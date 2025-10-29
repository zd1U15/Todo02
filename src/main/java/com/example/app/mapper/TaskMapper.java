package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.app.domain.Task;

// MyBatisがこのインターフェースを実装したクラスを自動で作ってくれる
@Mapper // このインターフェースがMyBatisのMapperであることを示す
public interface TaskMapper {

    // 全てのタスクを取得
    List<Task> findAll();

    // IDで1件のタスクを取得
    Task findById(Long id);

    // 新しいタスクを登録
    // (引数で渡したtaskオブジェクトのIDが、採番後に自動でセットされる)
    int insert(Task task);

    // タスクを更新
    int update(Task task);

    // タスクを削除
    int delete(Long id);
}