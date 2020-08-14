package com.whu.checky.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.whu.checky.domain.TypeTag;
import com.whu.checky.domain.UserHobby;
import com.whu.checky.mapper.TypeTagMapper;
import com.whu.checky.mapper.UserHobbyMapper;
import com.whu.checky.service.TypeTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.TypeMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("TypeTagService")
public class TypeTagServiceImpl implements TypeTagService {

    @Autowired
    private TypeTagMapper typeTagMapper;

    @Override
    public List<TypeTag> getTypeTagsBytypeId(String typeId) {
        return typeTagMapper.selectList(new EntityWrapper<TypeTag>().eq("type_id", typeId));
    }

    @Override
    public List<TypeTag> getTypeTagsBytagId(String tagId) {
        return typeTagMapper.selectList(new EntityWrapper<TypeTag>().eq("tag_id", tagId));
    }

    @Override
    public List<TypeTag> getAllTypeTags() {
        return typeTagMapper.queryAll();
    }

    @Override
    public int addTypeTag(TypeTag typeTag) {
       return typeTagMapper.insert(typeTag);
    }

    @Override
    public int delTypeTag(TypeTag typeTag) {
       return typeTagMapper.delete(new EntityWrapper<>(typeTag));
    }

    @Override
    public void updateTypeTag(String typeId,String tagId) {
        typeTagMapper.updateTypeTag(typeId,tagId);
    }
}
