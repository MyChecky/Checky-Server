package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.whu.checky.domain.Pay;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component(value = "PayMapper")
public interface PayMapper extends BaseMapper<Pay> {
//    @Select("SELECT PAY_ID as payId, PAY_ORDERINFO as payOrderinfo, PAY_USERID as \n" +
//            "        payUserid, PAY_MONEY as payMoney, PAY_TYPE as payType, PAY_TIME as \n"+
//            "         payTime, PAY_STATE as payState FROM pay \n" +
//            "        WHERE pay_time \n" +
//            "        between date (#{startDate}) and date (#{endDate}) \n" +
//            "        AND (pay_userid=#{userId})")
    List<Pay> queryUserScopePay(@Param("startDate")String startDate, @Param("endDate")String endDate,
                                @Param("userId")String userId);

    @Select("SELECT SUM(PAY_MONEY) FROM pay WHERE PAY_TYPE = #{payType}")
    Double selectSum(@Param("payType")String payType);

    Integer updatePaytoSuccess(@Param("nonceStr") String nonceStr);
}
