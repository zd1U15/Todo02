package com.example.app.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Task;
import com.example.app.dto.EventDTO;
import com.example.app.mapper.TaskMapper;

@Service // このクラスがService(ビジネスロジック層)であることを示す
public class TaskService {

    // @Autowired: Springが自動でTaskMapperのインスタンス(実体)をココに注入してくれる
    @Autowired
    private TaskMapper taskMapper;

    // DB操作が複数になる場合、まとめて成功/失敗させる(トランザクション管理)
    @Transactional(readOnly = true) // readOnly=true: 読み取り専用 (高速化)
    public List<Task> findAllTasks() {
        return taskMapper.findAll();
    }

    @Transactional(readOnly = true)
    public Task findTaskById(Long id) {
        return taskMapper.findById(id);
    }

    @Transactional // 読み取り専用ではない (書き込み)
    public void createTask(Task task) {
        taskMapper.insert(task);
    }

    @Transactional
    public void updateTask(Task task) {
        taskMapper.update(task);
    }

    @Transactional
    public void deleteTask(Long id) {
        taskMapper.delete(id);
    }

    // --- カレンダー用のロジック ---

    // 全タスクをカレンダー用のEventDTOリストに変換する
    @Transactional(readOnly = true)
    public List<EventDTO> findAllTasksAsEvents() {
        List<Task> tasks = taskMapper.findAll();

        // Java 8のStream APIを使って、TaskリストをEventDTOリストに変換
        return tasks.stream() // 1件ずつ処理
                .map(this::convertTaskToEventDTO) // 下のconvertTaskToEventDTOメソッドで変換
                .collect(Collectors.toList()); // 再度リストに集める
    }

    // TaskをEventDTOに変換し、色分けロジックを適用するメソッド
    private EventDTO convertTaskToEventDTO(Task task) {
        EventDTO event = new EventDTO();
        event.setId(task.getId());
        event.setTitle(task.getTitle());
        
        if (task.getStartDate() != null) {
        	// イベントの開始日を Task.startDate に設定
            event.setStart(task.getStartDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            // startDateがない場合は、従来のdueDateをstartとして使う (代替ロジック)
            event.setStart(task.getDueDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        if (task.getDueDate() != null) {
            // 期限日の翌日をイベントの終了日として設定
            event.setEnd(task.getDueDate().plusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE));
        }
        // クリック時の遷移先URL
        event.setUrl("/task/" + task.getId());

        // --- 色分けロジック ---
        // 優先度に基づいて色を決定
        String color = switch (task.getPriority() != null ? task.getPriority() : "") {
            case "高" -> "#d9534f"; // Bootstrapのdanger (赤)
            case "中" -> "#f0ad4e"; // Bootstrapのwarning (黄)
            case "低" -> "#5cb85c"; // Bootstrapのsuccess (緑)
            default -> "#5bc0de";  // Bootstrapのinfo (青)
        };

        // カテゴリに基づいて枠線のスタイルを変える (例)
        // (CSSで .category-work { border-width: 3px !important; } のように定義も可能)
        // ここでは色分けを優先度に集約し、backgroundColor/borderColorを同じ色に設定
        event.setBackgroundColor(color);
        event.setBorderColor(color);

        return event;
    }
}