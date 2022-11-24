package com.perfectrum.backend.mapper;

import com.perfectrum.backend.domain.entity.UserEntity;
import com.perfectrum.backend.dto.user.UserInfoDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInfoMapper extends GenericMapper<UserInfoDto, UserEntity> {

    @Override
    UserInfoDto toDto(UserEntity userEntity);

    @Override
    UserEntity toEntity(UserInfoDto userInfoDto);
}
