package com.example.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.app.domain.Task;
import com.example.app.dto.EventDTO;
import com.example.app.service.TaskService;

// @Controller: このクラスがSpring MVCのコントローラ(司令塔)であることを示す
@Controller
public class TaskController {

    // TaskServiceを使えるようにSpringに注入してもらう
    @Autowired
    private TaskService taskService;

    /**
     * カレンダー表示画面 (一覧)
     * http://localhost:8080/ にアクセスされた時の処理
     */
    @GetMapping("/")
    public String calendarView(Model model) {
        // カレンダー画面に、タスク追加用の空のTaskオブジェクトを渡しておく
        // (Thymeleafの th:object で使うため)
        model.addAttribute("newTask", new Task());
        
        // 表示するHTMLファイル名 (templates/calendar.html を指す)
        return "calendar";
    }

    /**
     * カレンダーに表示するタスクデータをJSONで返すAPI
     * (FullCalendar.js が /api/tasks にアクセスしてくる)
     */
    @GetMapping("/api/tasks")
    @ResponseBody // HTMLファイル名ではなく、戻り値(List<EventDTO>)をそのままJSONとして返す
    public List<EventDTO> getTasksAsEvents() {
        // Serviceに依頼して、色分け情報などを含んだEventDTOのリストを取得
        return taskService.findAllTasksAsEvents();
    }

    /**
     * タスク詳細・編集画面
     * http://localhost:8080/task/1 のようにアクセスされた時の処理
     */
    @GetMapping("/task/{id}")
    public String taskDetail(@PathVariable("id") Long id, Model model) {
        // @PathVariable("id"): URLの {id} の部分を引数の id 変数に入れてくれる
        Task task = taskService.findTaskById(id);
        
        // Model: HTML(Thymeleaf)にデータを渡すための箱
        // "task" という名前で、見つけてきたtaskオブジェクトをHTMLに渡す
        model.addAttribute("task", task);
        
        // 表示するHTMLファイル名 (templates/task-detail.html)
        return "task-detail";
    }

    /**
     * タスクの新規登録処理
     * (calendar.html のモーダルフォームから POST された時の処理)
     */
    @PostMapping("/task/new")
    public String createTask(@ModelAttribute Task newTask) {
        // @ModelAttribute: HTMLのフォームの入力内容を、Taskオブジェクト(newTask)に自動で詰めてくれる
        taskService.createTask(newTask);
        
        // 処理が終わったら、トップページ (カレンダー) にリダイレクト(飛ばす)
        return "redirect:/";
    }

    /**
     * タスクの更新処理
     * (task-detail.html の編集フォームから POST された時の処理)
     */
    @PostMapping("/task/edit")
    public String updateTask(@ModelAttribute Task task) {
        taskService.updateTask(task);
        
     // 編集が成功したら、TodoカレンダーのURL（例: /todos）へリダイレクトします
        return "redirect:/";
    }

    /**
     * タスクの削除処理
     * (task-detail.html の削除ボタンから POST された時の処理)
     */
    @PostMapping("/task/delete")
    public String deleteTask(@RequestParam("id") Long id) {
        // @RequestParam("id"): フォームから送られた "id" という名前のデータを引数 id に入れる
        taskService.deleteTask(id);
        
        // 処理が終わったら、トップページにリダイレクト
        return "redirect:/";
    }
}