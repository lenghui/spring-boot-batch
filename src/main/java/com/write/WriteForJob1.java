package com.write;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import com.model.People;

public class WriteForJob1 implements ItemWriter<People>{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// 批量插入
	@Override
	public void write(List<? extends People> items) throws Exception {
		String sql = "INSERT INTO PEOPLE(FIRST_NAME,LAST_NAME) VALUES(?,?)";
		jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setString(1, items.get(i).getFirstName());
				ps.setString(2, items.get(i).getLastName());
			}
			
			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return 0;
			}
		});
		
	}
	
	//一条一条插入效率太慢
//	@Override
//	public void write(List<? extends People> items) throws Exception {
//		System.out.println("*****************************");
//		for(People people : items) {
//			System.out.println(people.getFirstName());
//			jdbcTemplate.update("INSERT INTO PEOPLE(FIRST_NAME,LAST_NAME) VALUES(?,?)", new Object[] {people.getFirstName(), people.getLastName()});
//			
//		}
//		
//	}
	
	
}
