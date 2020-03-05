package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.Hobby;
import com.whu.checky.domain.UserHobby;
import com.whu.checky.mapper.HobbyMapper;
import com.whu.checky.mapper.ParameterMapper;
import com.whu.checky.mapper.UserHobbyMapper;
import com.whu.checky.service.HobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Service("HobbyService")
public class HobbyServiceImpl implements HobbyService {
    @Autowired
    private HobbyMapper hobbyMapper;

    @Autowired
    private UserHobbyMapper userHobbyMapper;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<String> getUserHobbies(String userId, int num) {
        List<UserHobby> userHobbies =
                userHobbyMapper.selectList(new EntityWrapper<UserHobby>().eq("user_id", userId));
        List<String> hobbies = new ArrayList<>();
        for (UserHobby userHobby : userHobbies) {
            Hobby hobby = hobbyMapper.selectById(userHobby.getHobbyId());
            hobbies.add(hobby.getHobbyValue());
        }
        for(int i = userHobbies.size(); i<num; i++){
            hobbies.add("");
        }
        return hobbies;
    }

    @Override
    public List<String> getHobbies(String userId, int num) {
        List<UserHobby> userHobbies =
                userHobbyMapper.selectList(new EntityWrapper<UserHobby>().eq("user_id", userId));
        List<String> hobbies = new ArrayList<>();
        List<String> hobbiesRet = new ArrayList<>();
        int total = hobbyMapper.selectCount(new EntityWrapper<>());
        Random random = new Random();
        for (UserHobby userHobby : userHobbies) {
            Hobby hobby = hobbyMapper.selectById(userHobby.getHobbyId());
            hobbies.add(hobby.getHobbyValue());
        }
        int i = 0;
        while (i < num) {
            int j = random.nextInt(total);
            String jHobbyV = hobbyMapper.selectById(j + "").getHobbyValue();
            if (!(hobbies.contains(jHobbyV) || hobbiesRet.contains(jHobbyV))) {
                hobbiesRet.add(jHobbyV);
                i++;
            }
        }
        return hobbiesRet;
    }

    @Override
    public void updateUserHobbies(String userId, List<String> hobbies) {
        userHobbyMapper.delete(new EntityWrapper<UserHobby>().eq("user_id", userId));
        for (String hobbyStr : hobbies) {
            List<Hobby> hobby = hobbyMapper.selectList(new EntityWrapper<Hobby>()
                    .eq("hobby_value", hobbyStr));
            List<UserHobby> userHobbies = userHobbyMapper.selectList(new EntityWrapper<UserHobby>()
                    .eq("hobby_id", hobby.get(0).getHobbyId())
                    .and()
                    .eq("user_id", userId));
            if(userHobbies.size()==0){
                UserHobby userHobby = new UserHobby();
                userHobby.setHobbyId(hobby.get(0).getHobbyId());
                userHobby.setUserId(userId);
                userHobby.setAddTime(dateFormat.format(new Date()));
                userHobbyMapper.insert(userHobby);
            }
        }
    }
}
