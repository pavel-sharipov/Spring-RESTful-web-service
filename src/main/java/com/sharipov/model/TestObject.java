package com.sharipov.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "objects")
public class TestObject {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column(nullable = false)
	private String title;
	@Column(nullable = false)
	private long value;

	public TestObject() {
	}

	public TestObject(int id, String title, long value) {

		this.id = id;
		this.title = title;
		this.value = value;
	}

	public TestObject(String title, long value) {
		this.title = title;
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TestObject [id=" + id + ", title=" + title + ", value=" + value + "]";
	}

}
