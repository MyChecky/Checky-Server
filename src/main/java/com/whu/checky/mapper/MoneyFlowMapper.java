package com.whu.checky.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.whu.checky.domain.MoneyFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component("moneyFlowMapper")
public interface MoneyFlowMapper extends BaseMapper<MoneyFlow> {
    @Select("SELECT flow_id as `flowId`, from_user_id as `fromUserId`," +
            "                  to_user_id as `toUserId`, flow_money as `flowMoney`, flow_time as `flowTime`,  \n" +
            "                   task_id as `taskId` FROM moneyflow\n" +
            "        WHERE flow_time\n" +
            "        between date (#{startDate}) and date (#{endDate})")
    List<MoneyFlow> queryAllScopeMoneyFlow(@Param("startDate") String startDate, @Param("endDate") String endDate);
    List<MoneyFlow> queryUserScopeMoneyFlow(String startDate, String endDate, String userId);

    String moneyFlowsWithNameSql = "SELECT flow_id as `flowId`, from_user_id as `fromUserId`,\n" +
            "       to_user_id as `toUserId`, flow_money as `flowMoney`, flow_time as `flowTime`, fromUserName as `fromUserName`,\n" +
            "       task_id as `taskId`,user.user_name as `toUserName` FROM\n" +
            "  (SELECT moneyflow.*,user.user_name as `fromUserName`\n" +
            "   from moneyflow,user where moneyflow.from_user_id=user.user_id) as f,user where f.to_user_id=user.user_id";

    @Select(moneyFlowsWithNameSql + " ${ew.sqlSegment}")
    List<MoneyFlow> getMoneyFlowsWithName(@Param("ew") Wrapper wrapper);

    @Select(moneyFlowsWithNameSql + " ${ew.sqlSegment}")
    List<MoneyFlow> getMoneyFlowsWithName(@Param("ew") Wrapper wrapper, Pagination page);

    List<MoneyFlow> queryMoneyFolwByUserName(@Param("username")String username);
}
