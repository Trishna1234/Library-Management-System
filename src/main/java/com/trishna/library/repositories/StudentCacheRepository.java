package com.trishna.library.repositories;

import com.trishna.library.dtos.GetStudentResponse;
import com.trishna.library.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StudentCacheRepository {
    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    public GetStudentResponse get(Integer studentId){
        GetStudentResponse object = (GetStudentResponse) redisTemplate.opsForValue().get(getKey(studentId));
        return (object == null) ? null :  object;
    }


    public void delete(Integer studentId){
        redisTemplate.opsForValue().getAndDelete(getKey(studentId));
    }

    public void getSet(Integer studentId, GetStudentResponse student){
        redisTemplate.opsForValue().getAndSet(getKey(studentId), student);
    }
    public void set(GetStudentResponse student){
        redisTemplate.opsForValue().set(getKey(student.getId()), student);
    }

    private  String getKey(Integer studentId){
        return Constants.STUDENT_KEY_PREFIX + studentId;
    }
}
