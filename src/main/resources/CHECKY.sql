CREATE DATABASE `checky`;

CREATE table checky.user(
user_id varchar(36) not null unique,
user_name varchar(20) not null,
user_gender varchar(10) not null check(user_gender = 'male' or user_gender = 'female'),
user_avatar varchar(255) not null,
user_time varchar(19) not null,
user_credit int not null default 100,
user_money double default 0,
test_money double default 100,
task_num int not null default 0,
task_num_suc int not null default 0,
supervise_num int not null default 0,
supervise_num_min int not null default 0,
wantpush int not null default 0,
longtitude Decimal(10,7),
latitude Decimal(10,7),
session_id varchar(40) default null,
hobby varchar(255) null,
reported_total int null,
reported_passed int null,
report_total int null,
report_passed int null,
constraint `pk_user` primary key(user_id)  
);

create table checky.task_type(
type_id varchar(36) not null unique,
type_content varchar(50) not null,
constraint `pk_type` primary key(type_id)  
);

create table checky.suggestion(
suggestion_id varchar(36) not null unique,
user_id varchar(36) not null,
suggestion_content varchar(50) not null,
suggestion_time varchar(19) not null,
suggestion_state varchar(10) check (suggestion_state = 'waiting' or 'processed'),
constraint `pk_suggestion` primary key(suggestion_id),
constraint `fk_suggestion_to_user` foreign key(user_id) references checky.user(user_id) on update cascade
);

create table checky.task(
task_id varchar(36) not null unique,
user_id varchar(36) not null,
type_id varchar(36) not null,
task_title varchar(50) not null,
task_content varchar(255) not null default '暂时没有具体内容',
task_start_time varchar(19) not null,
task_end_time varchar(19) not null,
task_state varchar(10) not null check(task_state = 'save'or'nomatch'or'during'or'success' or 'fail' or 'complete') default 'nomatch',
task_money double not null default 0,
supervisor_num int not null default 0,
refund_money double,
system_benifit double,
check_num int not null,
check_times int not null default 0,
check_frec varchar(7) not null default '0000000',
if_test int(1) not null,
check_pass int default 0,
min_pass double,
real_pass double,
match_num int,
min_check double not null,
min_check_type varchar(11) not null check(min_check_type = 'proportion' or 'number'),
supervisor_type int(1) not null default 0,
if_area int(1) not null default 0,
if_hobby int(1) not null default 0,
constraint `pk_task` primary key(task_id),
constraint `fk_task_to_user` foreign key(user_id) references checky.user(user_id) on update cascade,  
constraint `fk_task_to_type` foreign key(type_id) references checky.task_type(type_id) on update cascade
);

create table checky.check(
check_id varchar(36) not null unique,
user_id varchar(36) not null,
task_id varchar(36) not null,
check_time varchar(19) not null,
check_state varchar(10) check(checky.check.check_state = 'pass' or 'deny'or 'unknown'),
supervise_num int not null default 0,
pass_num int not null default 0,
constraint `pk_check` primary key(check_id),
constraint `fk_check_to_user` foreign key(user_id) references checky.user(user_id) on update cascade,
constraint `fk_check_to_task` foreign key(task_id) references checky.task(task_id) on update cascade
);

create table checky.record(
record_id varchar(36) not null unique,
check_id varchar(36) not null,
essay_id varchar(36) not null,
record_type varchar(10) not null check(record_type = 'image' or 'sound' or 'video'),
file_addr varchar(255) not null,
record_time varchar(19) not null,
record_content varchar(1024),
constraint `pk_record` primary key(record_id),
constraint `fk_record_to_check` foreign key(check_id) references checky.check(check_id) on update cascade,
constraint `fk_record_to_essay` foreign key(essay_id) references checky.essay(essay_id) on update cascade
);

create table checky.task_supervisor(
task_id varchar(36) not null,
supervisor_id varchar(36) not null,
benefit double not null default 0,
add_time varchar(19) not null,
remove_time varchar(19),
remove_reason varchar(255),
report_num int default 0,
bad_num int default 0,
supervise_num int not null default 0,
constraint `fk_task_has_supervisor` foreign key(task_id) references checky.task(task_id) on update cascade,
constraint `fk_supervisor_has_task` foreign key(supervisor_id) references checky.user(user_id) on update cascade 
);

create table checky.supervise(
supervise_id varchar(36) not null unique,
check_id varchar(36) not null,
supervisor_id varchar(36) not null,
supervise_time varchar(19) not null,
supervise_content varchar(255) not null default '暂时没有具体内容',
supervise_state varchar(10) not null check(supervise_state = 'pass' or 'deny'), 
constraint `pk_supervise` primary key(supervise_id),
constraint `fk_supervise_to_check` foreign key(check_id) references checky.check(check_id) on update cascade,
constraint `fk_supervise_to_supervisor` foreign key(supervisor_id) references checky.task_supervisor(supervisor_id) on update cascade 
);

CREATE TRIGGER `checky`.`updateUserAfterInsert` AFTER INSERT ON `supervise` FOR EACH ROW
update checky.user set checky.user.supervise_num = checky.user.supervise_num + 1
where user.user_id = new.supervisor_id;

create table checky.appeal(
appeal_id varchar(36) not null unique,
user_id varchar(36) not null,
task_id varchar(36) not null,
check_id varchar(36) not null,
appeal_content varchar(255) not null default '暂时没有具体内容',
appeal_time varchar(19) not null,
process_result varchar(20),
process_time varchar(19),
constraint `pk_appeal` primary key(appeal_id),
constraint `fk_appeal_to_user` foreign key(user_id) references checky.user(user_id) on update cascade,
constraint `fk_appeal_to_task` foreign key(task_id) references checky.task(task_id) on update cascade,
constraint `fk_appeal_to_check` foreign key(check_id) references checky.check(check_id) on update cascade  
);

create table checky.essay(
essay_id varchar(36) not null unique,
user_id varchar(36) not null,
essay_content varchar(1024) not null default '暂时没有具体内容',
essay_time varchar(19) not null,
like_num int default 0 not null,
comment_num int default 0 not null,
version int not null default 0,
longtitude decimal(10,7),
latitude decimal(10,7),
constraint `pk_essay` primary key(essay_id),
constraint `fk_essay_to_user` foreign key(user_id) references checky.user(user_id) on update cascade
);

create table checky.like(
user_id varchar(36) not null,
essay_id varchar(36) not null,
add_time varchar(19) not null,
constraint `pk_like` primary key(user_id, essay_id),
constraint `fk_like_to_essay` foreign key(essay_id) references checky.essay(essay_id) on update cascade,
constraint `fk_like_to_user` foreign key(user_id) references checky.user(user_id) on update cascade
);

create trigger checky.like_after_insert after insert on `like` for each row
begin
    update essay set like_num = like_num + 1 where essay_id = new.essay_id;
end ;

create trigger checky.like_before_delete before delete on `like` for each row
begin
    update essay set like_num = like_num - 1 where essay_id = old.essay_id;
end ;

create table checky.comment(
comment_id varchar(36) not null unique,
essay_id varchar(36) not null,
user_id varchar(36) not null,
comment_content varchar(255) not null default '暂时没有具体内容',
comment_time varchar(19) not null,
comment_type varchar(10) not null check(comment_type = 'like' or 'comment' or 'both'),
constraint `pk_comment` primary key(comment_id),
constraint `fk_comment_to_essay` foreign key(essay_id) references checky.essay(essay_id) on update cascade,
constraint `fk_comment_to_user` foreign key(user_id) references checky.user(user_id) on update cascade
);

create trigger checky.comment_after_insert after insert on `comment` for each row
begin
    update essay set comment_num = comment_num + 1 where essay_id = new.essay_id;
end ;

create trigger checky.comment_before_delete before delete on `comment` for each row
begin
    update essay set comment_num = comment_num - 1 where essay_id = old.essay_id;
end ;

create table checky.report(
report_id varchar(36) not null unique,
user_id varchar(36),
user_reported_id varchar(36) not null,
supervisor_id varchar(36) not null,
task_id varchar(36) not null,
check_id varchar(36) not null,
essay_id varchar(36) not null,
report_time varchar(19) not null, 
report_content varchar(255) not null default '暂时没有具体内容',
report_type varchar(1) not null check(report_type = '0' or '1' or '2'), 
process_result varchar(20) default 'toProcess',
process_time varchar(19),
constraint `pk_report` primary key(report_id),
constraint `fk_report_to_user` foreign key(user_id) references checky.user(user_id) on update cascade,
constraint `fk_report_to_supervisor` foreign key(supervisor_id) references checky.user(user_id) on update cascade,
constraint `fk_report_to_task` foreign key(task_id) references checky.task(task_id) on update cascade,
constraint `fk_report_to_check` foreign key(check_id) references checky.check(check_id) on update cascade,
constraint `fk_report_to_essay` foreign key(essay_id) references checky.essay(essay_id) on update cascade
);

create table checky.user_friend(
to_user_id varchar(36) not null,
from_user_id varchar(36) not null,
add_time varchar(19) not null,
coo_num int not null default 0,
constraint `fk_to_user` foreign key(to_user_id) references checky.user(user_id) on update cascade,
constraint `fk_from_user` foreign key(from_user_id) references checky.user(user_id) on update cascade
);

create table checky.moneyflow(
flow_id varchar(36) not null unique,
user_id varchar(36) not null,
if_test int(1) not null,
flow_io varchar(1) not null,
flow_money double not null,
flow_time varchar(19) not null,
flow_type varchar(8) not null check (flow_type = 'init' or 'pay' or 'refund' or 'benefit'),
task_id varchar(36) null,
remark varchar(100) null,
constraint `pk_moneyflow` primary key(flow_id),
constraint `fk_flow_to_user` foreign key(to_user_id) references checky.user(user_id) on update cascade,
constraint `fk_flow_from_user` foreign key(from_user_id) references checky.user(user_id) on update cascade
);


create table checky.administrator(
user_id int not null unique,
user_name varchar(20) not null,
user_password varchar(20) not null,
session_id varchar(40) null,
user_tel varchar(11) null,
user_email varchar(40) null,
constraint `pk_administrator` primary key(user_id)
);

CREATE table checky.parameter(
param_id varchar(36) not null unique,
param_name varchar(20) not null,
param_value varchar(20) not null
constraint `pk_parameter` primary key(param_id)
);

CREATE table checky.menu(
menu_id varchar(36) not null unique,
menu_name varchar(20) not null,
menu_url varchar(50) null,
flag int(1) not null,
constraint `pk_menu` primary key(menu_id)
)

CREATE table check.pay(
pay_id varchar(36) not null unique,
pay_orderinfo varchar(36) not null,
pay_userid varchar(36) not null,
pay_money double not null,
pay_type varchar(10) not null check(pay_type = 'pay' or 'withdraw'),
paytime varchar(20),
constraint `pk_pay` primary key(pay_id)
)
