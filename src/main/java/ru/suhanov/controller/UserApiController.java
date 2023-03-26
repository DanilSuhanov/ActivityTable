package ru.suhanov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.suhanov.exception.ExceptionInfo;
import ru.suhanov.model.Member;
import ru.suhanov.model.Role;
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

    private HttpStatus checkSecurity(String username) {
        User user = userService.findUserByUsername(username);
        if (user == null)
            return HttpStatus.UNAUTHORIZED;
        if (user.getRoles().stream().map(Role::getAuthority).noneMatch(a -> a.equals("ROLE_ADMIN")))
            return HttpStatus.NON_AUTHORITATIVE_INFORMATION;
        return HttpStatus.OK;
    }

    private HttpStatus checkSecurity(User user) {
        if (user == null)
            return HttpStatus.UNAUTHORIZED;
        if (user.getRoles().stream().map(Role::getAuthority).noneMatch(a -> a.equals("ROLE_ADMIN")))
            return HttpStatus.NON_AUTHORITATIVE_INFORMATION;
        return HttpStatus.OK;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> list(Principal principal) {
        HttpStatus status = checkSecurity(principal.getName());
        if (!status.equals(HttpStatus.OK)) {
            return new ResponseEntity<>(status);
        } else {
            return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ExceptionInfo> pageDelete(@PathVariable("id") Long id, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        HttpStatus status = checkSecurity(user);

        if (!status.equals(HttpStatus.OK) && !Objects.equals(user.getId(), id)) {
            return new ResponseEntity<>(status);
        } else {
            userService.deleteUserById(id);
            return new ResponseEntity<>(new ExceptionInfo("User deleted"), HttpStatus.OK);
        }
    }

    @GetMapping("users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<User> getUserByUsername (Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        return new ResponseEntity<>(user,HttpStatus.OK);
    }

    @PostMapping("/user/editPassword")
    public ResponseEntity<ExceptionInfo> pageEdit(@RequestBody String password, Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        user.setPassword(password);

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

    @GetMapping("/user/implementers")
    public ResponseEntity<List<User>> getImplementers(Principal principal) {
        return new ResponseEntity<>(userService.findUserByUsername(principal
                .getName()).getImplementers(), HttpStatus.OK);
    }

    @GetMapping("/task/{id}/members")
    public ResponseEntity<List<Member>> getMembersInTask(@PathVariable long id) {
        return new ResponseEntity<>(taskService.findTaskById(id).getMembers(), HttpStatus.OK);
    }

    @PostMapping("/task/{id}/addUser")
    public ResponseEntity<ExceptionInfo> addUserToTask(@PathVariable long id, @RequestBody String username) {
        Task task = taskService.findTaskById(id);
        User user = userService.findUserByUsername(username);

        Member member = new Member();
        member.setTaskRole(TaskRole.Исполнитель);
        member.setTask(task);
        member.setUser(user);

        memberService.addNewMember(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/task/{id}/deleteMember")
    public ResponseEntity<ExceptionInfo> deleteMember(@PathVariable long id, @RequestBody String username) {
        Task task = taskService.findTaskById(id);
        Member member = userService.findUserByUsername(username).getMembers()
                .stream().filter(m -> m.getTask().equals(task))
                .findFirst().orElse(null);
        memberService.deleteMember(member);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/task/{id}/completeness")
    public ResponseEntity<ExceptionInfo> setCompleteness(@PathVariable long id, @RequestBody int completeness) {
        Task task = taskService.findTaskById(id);
        task.setCompleteness(completeness);

        taskService.update(task);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/user/getPassword")
    public ResponseEntity<String> getUserPassword(Principal principal) {
        User user = userService.findUserByUsername(principal.getName());
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        else
            return new ResponseEntity<>(user.getPassword(), HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}/exit")
    public ResponseEntity<ExceptionInfo> taskExit(Principal principal, @PathVariable Long id) {
        User user = userService.findUserByUsername(principal.getName());
        Task task = taskService.findTaskById(id);
        Member member = taskService.findMemberByUserAndTaskId(user, task);

        if (member.getTaskRole().equals(TaskRole.Исполнитель)) {
            memberService.deleteMember(member);
        } else {
            taskService.deleteTask(task);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/tasks/{id}/verification")
    public ResponseEntity<ExceptionInfo> verificationTask(@PathVariable Long id, Principal principal) {
        Task task = taskService.findTaskById(id);
        User user = userService.findUserByUsername(principal.getName());
        Member member = taskService.findMemberByUserAndTaskId(user, task);

        if (member.getTaskRole().equals(TaskRole.Руководитель)) {
            task.setVerification(true);
            taskService.update(task);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}