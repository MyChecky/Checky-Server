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
public interface  MoneyFlowMapper extends BaseMapper<MoneyFlow> {
    //这里还不知道改的对不对
    @Select("SELECT flow_id as `flowId`, user_id as `userId`," +
            "       flow_money as `flowMoney`, flow_time as `flowTime`,  \n" +
            "       task_id as `taskId`, if_test as ifTest, flow_io as flowIO, \n" +
            "       flow_type as flowType FROM moneyflow\n" +
            "        WHERE flow_time\n" +
            "        between date (#{startDate}) and date (#{endDate})")
    List<MoneyFlow> queryAllScopeMoneyFlow(@Param("startDate") String startDate, @Param("endDate") String endDate);
//    @Select("SELECT flow_id as `flowId`, user_id as `userId`," +
//            "       flow_money as `flowMoney`, flow_time as `flowTime`,  \n" +
//            "       task_id as `taskId`, if_test as ifTest, flow_io as flowIO, \n" +
//            "       flow_type as flowType FROM moneyflow\n" +
//            "        WHERE flow_time\n" +
//            "        between date (#{startDate}) and date (#{endDate})" +
//            "        AND (user_id=#{userId})")
    List<MoneyFlow> queryUserScopeMoneyFlow(@Param("startDate") String startDate, @Param("endDate")String endDate,
                                            @Param("userId")String userId);
    @Select("SELECT SUM(moneyflow.flow_money) FROM moneyflow,task WHERE moneyflow.if_test = #{ifTest} AND moneyflow.flow_io = #{flowIo} \n" +
            "AND moneyflow.task_id = task.task_id AND task.task_state = 'complete'")
    Double selectSum(@Param("ifTest") int ifTest, @Param("flowIo") String flowIo);
//    String moneyFlowsWithNameSql = "SELECT flow_id AS `flowId`, f.user_id AS `userId`, flow_money AS `flowMoney`, \n" +
//            "flow_time AS `flowTime`, task_id AS `taskId` \n" +
//            ", userName AS `userName`, flow_io AS `flowIo`, flow_type AS `flowType`, remark AS `remark` \n"+
//            "FROM (SELECT moneyflow.*,user.user_name AS `userName` FROM moneyflow,\n" +
//            "USER WHERE moneyflow.user_id=user.user_id) AS f";
//
//    @Select(moneyFlowsWithNameSql + " ${ew.sqlSegment}")
//    List<MoneyFlow> getMoneyFlowsWithName(@Param("ew") Wrapper wrapper);
//
//    @Select(moneyFlowsWithNameSql + " ${ew.sqlSegment}")
//    List<MoneyFlow> getMoneyFlowsWithName(@Param("ew") Wrapper wrapper, Pagination page);

    List<MoneyFlow> queryMoneyFolwByUserName(@Param("username")String username);
}
