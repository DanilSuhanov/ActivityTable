package ru.suhanov.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.suhanov.exception.ExceptionInfo;
import ru.suhanov.model.Member;
import ru.suhanov.model.task.Task;
import ru.suhanov.model.User;
import ru.suhanov.model.task.TaskMessage;
import ru.suhanov.service.interfaces.MemberService;
import ru.suhanov.service.interfaces.TaskMessageService;
import ru.suhanov.service.interfaces.TaskService;
import ru.suhanov.service.interfaces.UserService;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;
    private final TaskService taskService;
    private final MemberService memberService;
    private final TaskMessageService taskMessageService;

    @Autowired
    public UserApiController(UserService userService, TaskService taskService, MemberService memberService, TaskMessageService taskMessageService) {
        this.userService = userService;
        this.taskService = taskService;
        this.memberService = memberService;
        this.taskMessageService = taskMessageService;
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

    @PutMapping("/users/{id}")
    public ResponseEntity<ExceptionInfo> pageEdit(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
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
        return new ResponseEntity<>(taskService.findAllMessagesByTaskId(id), HttpStatus.OK);
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
}