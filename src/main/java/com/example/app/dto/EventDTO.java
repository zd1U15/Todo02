package com.example.app.dto;

import lombok.Data;

//FullCalendar.js に渡すためのイベント情報
@Data
public class EventDTO {

 private Long id;          // タスクのID (クリック時の遷移先で使用)
 private String title;     // カレンダーに表示するタイトル
 private String url;       // クリック時の遷移先URL
 private String backgroundColor; // 背景色 (色分け用)
 private String borderColor;     // 枠線の色 (色分け用)
 
private String start;
private String end; 
}