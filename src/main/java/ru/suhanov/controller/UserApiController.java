package ru.suhanov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import ru.suhanov.exception.ExceptionInfo;
import ru.suhanov.model.Member;
import ru.suhanov.model.enam.TaskRole;
import ru.suhanov.model.request.ImpRequest;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.model.task.TaskMessage;
import ru.suhanov.service.interfaces.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;
    private final TaskService taskService;
    private final MemberService memberService;
    private final TaskMessageService taskMessageService;
    private final ImpRequestService impRequestService;

    @Autowired
    public UserApiController(UserService userService, TaskService taskService, MemberService memberService, TaskMessageService taskMessageService, ImpRequestService impRequestService) {
        this.userService = userService;
        this.taskService = taskService;
        this.memberService = memberService;
        this.taskMessageService = taskMessageService;
        this.impRequestService = impRequestService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> list() {
        return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ExceptionInfo> pageDelete(@PathVariable("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(new ExceptionInfo("User deleted"), HttpStatus.OK);
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getUser (@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByUsername (Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<ExceptionInfo> pageEdit(@RequestBody Map<String, String> userData, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        user.setUsername(userData.get("username"));
        user.setPassword(userData.get("password"));

        userService.editUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getTasks(Principal principal) {
        return new ResponseEntity<>(userService.parsUser(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/task/{id}/member")
    public ResponseEntity<Member> getMemberByTask(Principal principal, @PathVariable long id) {
        return new ResponseEntity<>(taskService.findMemberByUserAndTaskId(userService
                .findUserByUsername(principal.getName()), id), HttpStatus.OK);
    }

    @PostMapping("/task/{id}/message")
    public ResponseEntity<ExceptionInfo> createNewTaskMessage(@PathVariable long id, Principal principal, @RequestBody String content) {
        TaskMessage taskMessage = new TaskMessage();
        taskMessage.setTask(taskService.findTaskById(id));
        taskMessage.setDate(new Date());
        taskMessage.setMember(taskService.findMemberByUserAndTaskId(userService
                .findUserByUsername(principal.getName()), id));
        taskMessage.setContent(content);
        taskMessageService.addNewTaskMessage(taskMessage);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/task/{id}/messages")
    public ResponseEntity<List<TaskMessage>> getAllMessages(@PathVariable long id) {
        taskService.findAllMessagesByTaskId(id)
                .stream().sorted(Comparator.comparing(TaskMessage::getDate))
                .collect(Collectors.toList()).forEach(t -> System.out.println(t.getContent()));
        return new ResponseEntity<>(taskService.findAllMessagesByTaskId(id)
                .stream().sorted(Comparator.comparing(TaskMessage::getDate))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("/usernameByMessageId/{id}")
    public ResponseEntity<String> getUsernameByMessageId(@PathVariable long id) {
        return new ResponseEntity<>(taskMessageService.findTaskMessageById(id)
                .getMember().getUser().getUsername(), HttpStatus.OK);
    }

    @GetMapping("/username")
    public ResponseEntity<String> getUsername(Principal principal) {
        return new ResponseEntity<>(principal.getName(), HttpStatus.OK);
    }

    @PostMapping("/task/deleteMessageById")
    public ResponseEntity<ExceptionInfo> deleteMessageById(@RequestBody long id){
        taskMessageService.deleteTaskMessageById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/getAllImp")
    public ResponseEntity<List<User>> findAllImp(Principal principal) {
        return new ResponseEntity<>(userService.findUserByUsername(principal.getName())
                .getImplementers(), HttpStatus.OK);
    }

    @PostMapping("/user/implement/delete")
    public ResponseEntity<ExceptionInfo> deleteImplement(@RequestBody long id, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        List<User> implementers = user.getImplementers().stream()
                .filter(u -> !u.getId().equals(id)).collect(Collectors.toList());
        user.setImplementers(implementers);
        userService.editUser(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/implementers/add")
    public ResponseEntity<ExceptionInfo> addImplementer(@RequestBody String username, Principal principal) {
        User imp = userService.findUserByUsername(username);
        if (imp == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            ImpRequest impRequest = new ImpRequest();
            impRequest.setImp(imp);
            impRequest.setSender(userService.findUserByUsername(principal.getName()));
            impRequest.setDate(new Date());

            impRequestService.addNewImpRequest(impRequest);

            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/user/impInvites")
    public ResponseEntity<List<ImpRequest>> findAllImpInvites(Principal principal) {
        return new ResponseEntity<>(userService.findUserByUsername(principal.getName()).getInvites(), HttpStatus.OK);
    }

    @PostMapping("/user/invite/reject")
    public ResponseEntity<ExceptionInfo> rejectInvite(@RequestBody long inviteId) {
        impRequestService.deleteImpRequestById(inviteId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/user/invite/accept")
    public ResponseEntity<ExceptionInfo> acceptInvite(@RequestBody long inviteId, Principal principal) {
        ImpRequest impRequest = impRequestService.findRequestById(inviteId);
        User sender = impRequest.getSender();
        User imp = userService.findUserByUsername(principal.getName());
        sender.addSubordinates(imp);

        impRequestService.deleteImpRequestById(impRequest.getId());
        userService.update(sender);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/tasks/create")
    public ResponseEntity<ExceptionInfo> createNewTask(@RequestBody Task task, Principal principal) {
        taskService.addNewTask(task);

        Member member = new Member();
        member.setUser(userService.findUserByUsername(principal.getName()));
        member.setTask(task);
        member.setTaskRole(TaskRole.Руководитель);

        memberService.addNewMember(member);

        task.addMember(member);
        taskService.update(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}