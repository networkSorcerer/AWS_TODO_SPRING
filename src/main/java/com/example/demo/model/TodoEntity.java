package com.example.demo.model;

//import org.hibernate.annotations.GenericGenerator; 예전버전이어서 사용이 권장되지 않음
import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="Todo")
public class TodoEntity {
	@Id
	@GeneratedValue(generator="system-uuid")//시퀀스 및 자동 설정 관련된듯
	//@GenericGenerator(name="system-uuid", strategy="uuid")
	@UuidGenerator  
	private String id;
	private String title;
	private boolean done;
	private String userId;
	
}
