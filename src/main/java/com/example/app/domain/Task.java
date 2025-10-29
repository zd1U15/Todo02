package com.example.app.domain;

import java.time.LocalDate;

import lombok.Data;

// 1つのタスクの情報を保持するクラス (JavaBeans / POJO)
@Data
public class Task {

	// フィールド (DBのカラムに対応)
	private Long id; // ID
	private String title; // タイトル
	private String details; // 詳細
	private LocalDate dueDate; // 期日 (Java 8以降の日付型)
	private String category; // カテゴリ
	private String priority; // 優先度
	private boolean completed; // 完了フラグ
	
	private LocalDate startDate;

	// --- 以下、Getter/Setter ---
	// (カプセル化のため、フィールドはprivateにし、メソッド経由でアクセスする)

}