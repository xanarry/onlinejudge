DROP DATABASE IF EXISTS oj;
CREATE DATABASE IF NOT EXISTS oj charset = "utf8";
USE oj;

DROP TABLE IF EXISTS t_problem;
CREATE TABLE t_problem (
  /*题目信息*/
  `problem_id`    INT   NOT NULL auto_increment,      /*题目id,主键*/
  `title`         VARCHAR(100)   NOT NULL DEFAULT "", /*题目标题*/
  `desc`          text NOT NULL,  /*题目描述*/
  `input_desc`    text NOT NULL,  /*输入描述*/
  `output_desc`   text NOT NULL,  /*输出描述*/
  `input_sample`  text NOT NULL,  /*输入样例*/
  `output_sample` text NOT NULL,  /*输出样例*/
  `hint`          text NOT NULL,  /*提示*/
  `source`        text NOT NULL,  /*来源*/
  `background`    TEXT not null,
  `create_time`   bigint NOT NULL DEFAULT 0,     /*创建时间*/
  `static_lang_time_limit` mediumint NOT NULL default 1000,  /*静态语言时间限制*/
  `static_lang_mem_limit` mediumint NOT NULL default 65535,  /*静态语言内存限制*/
  `dynamic_lang_time_limit` mediumint NOT NULL default 2000, /*动态语言时间限制*/
  `dynamic_lang_mem_limit` mediumint NOT NULL default 131070, /*动态语言内存限制*/
  `accepted`      INT   NOT NULL DEFAULT 0,  /*通过人数*/
  `submitted`      INT   NOT NULL DEFAULT 0,  /*提交次数*/
  primary key(`problem_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE=InnoDB;


DROP TABLE IF EXISTS t_test_point;
CREATE TABLE t_test_point (
  /*测试点数据*/
  `problem_id`         INT          NOT NULL, /*题目id,主键*/
  `test_point_id`      INT          NOT NULL, /*测试点编号*/
  `input_text_path`    VARCHAR(256) NOT NULL, /*输入文件路径*/
  `input_text_length`  INT          NOT NULL, /*输入类容长度*/
  `output_text_path`   VARCHAR(256) NOT NULL, /*输出文件路径*/
  `output_text_length` INT          NOT NULL, /*输出文件长度*/
  PRIMARY KEY (`problem_id`, `test_point_id`)
) DEFAULT CHARSET = "utf8" ENGINE = InnoDB;






DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
  /*用户信息*/
  `user_id`       INT  NOT NULL auto_increment, /*用户id*/
  `email`         VARCHAR(64)    NOT NULL,     /*邮箱*/
  `user_name`     VARCHAR(64)     NOT NULL,     /*用户名*/
  `password`      VARCHAR(40)     NOT NULL,     /*密码*/
  `register_time` bigint  NOT NULL,  /*注册时间*/
  `last_login_time` bigint NOT NULL, /*最后登录时间*/
  `user_type`    TINYINT NOT NULL DEFAULT 0,    /*用户类型*/
  `prefer_language` VARCHAR(16) NOT NULL DEFAULT -1,/*偏好语言*/
  `accepted`      INT  NOT NULL DEFAULT 0,      /*通过次数*/
  `submitted`     INT  NOT NULL DEFAULT 0,       /*提交次数*/
  `bio`         VARCHAR(64),                 /*签名*/
  `send_code`    TINYINT,                       /*是否发送通过的代码到邮箱*/
    PRIMARY KEY(`user_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE=InnoDB;








DROP TABLE IF EXISTS t_submit_record;
CREATE TABLE t_submit_record (
  /*用户提交记录*/
  `submit_id`    INT AUTO_INCREMENT AUTO_INCREMENT                                                                                                                                                                                         NOT NULL, /*提交id*/
  `user_id`      INT                                                                                                                                                                                                                       NOT NULL, /*用户*/
  `problem_id`   INT                                                                                                                                                                                                                       NOT NULL, /*题目*/
  `contest_id`   INT                                                                                                                                                                                                                       NOT NULL DEFAULT 0, /*比赛ID,正常做题为0, 在比赛中的话该值设为比赛ID*/
  `result`       VARCHAR(32)  NOT NULL, /*运行结果*/
  `language`     VARCHAR(16)  NOT NULL, /*代码语言*/
  `source_code`  text,                         /*源代码*/
  `code_length`  MEDIUMINT DEFAULT 0,          /*代码长度*/
  `time_consume` SMALLINT                                                                                                                                                                                                                  NOT NULL DEFAULT 0, /*耗时*/
  `mem_consume`  MEDIUMINT                                                                                                                                                                                                                 NOT NULL DEFAULT 0, /*耗内存*/
  `submit_time`  BIGINT                                                                                                                                                                                                                    NOT NULL DEFAULT 0, /*提交时间*/
  `judge_time`   BIGINT                                                                                                                                                                                                                    NOT NULL DEFAULT 0, /*评测时间*/
  primary key(`submit_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE = InnoDB;


DROP TABLE IF EXISTS t_compile_info;
CREATE TABLE t_compile_info (
  /*提交代码编译结果*/
  `submit_id` INT NOT NULL,           /*提交id*/
  `compile_result` text,   /*编译结果,成功为空,失败保存错误信息*/
  PRIMARY KEY(`submit_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;


DROP TABLE IF EXISTS t_system_error;
CREATE TABLE t_system_error (
  /*保持系统错误的信息*/
  `submit_id` INT NOT NULL,       /*提交id*/
  `error_message` VARCHAR(100),   /*系统返回的错误信息, 用于诊断系统故障*/
  PRIMARY KEY(`submit_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;


DROP TABLE IF EXISTS t_judge_detail;
CREATE TABLE t_judge_detail (
  /*保存每个测试点的详细信息*/
  `submit_id`     INT                                                                                                                                                                                                                       NOT NULL,             /*提交id*/
  `test_point_id` TINYINT                                                                                                                                                                                                                   NOT NULL, /*测试点id*/
  `time_consume`  MEDIUMINT                                                                                                                                                                                                                 NOT NULL DEFAULT 0, /*耗时*/
  `mem_consume`   MEDIUMINT                                                                                                                                                                                                                 NOT NULL DEFAULT 0, /*耗内存*/
  `return_val`    INT                                                                                                                                                                                                                       NOT NULL DEFAULT 0, /*进程返回值*/
  `result`       VARCHAR(32)  NOT NULL, /*运行结果*/
  PRIMARY KEY (`submit_id`, `test_point_id`)
) DEFAULT charset = "utf8" auto_increment = 1 ENGINE = InnoDB;








DROP TABLE IF EXISTS t_contest;
CREATE TABLE t_contest (
  /*比赛信息*/
  `contest_id` INT auto_increment NOT NULL,        /*比赛id*/
  `title` VARCHAR(100) NOT NULL,    /*比赛名称*/
  `desc` VARCHAR(400),               /*比赛介绍*/
  `start_time` bigint NOT NULL,        /*开始时间*/
  `end_time` bigint NOT NULL,          /*结束时间*/
  `register_start_time` bigint NOT NULL, /*报名开始时间*/
  `register_end_time` bigint NOT NULL,   /*报名结算时间*/
  `password` VARCHAR(20) DEFAULT "",      /*比赛密码, 为空的是否公开, 不为空的事就加密*/
  `sponsor` VARCHAR(64) NOT NULL,     /*发起人*/
  `contest_type` ENUM("OI", "ACM") DEFAULT "ACM", /*赛制*/
  `create_time`  BIGINT DEFAULT 0 NOT NULL, /*比赛创建时间*/
  PRIMARY KEY(`contest_id`)
) DEFAULT charset = "utf8" auto_increment=1  ENGINE = InnoDB;


DROP TABLE IF EXISTS t_contest_problem;
CREATE TABLE t_contest_problem (
  /*比赛题目*/
  `contest_id` INT NOT NULL,  /*比赛id*/
  `problem_id` INT NOT NULL,  /*题目全局id*/
  `title`      VARCHAR(100),  /*题目标题(冗余)*/
  `inner_id`   VARCHAR(2),    /*题目在本次比赛中的id*/
  `accepted`   INT NOT NULL DEFAULT 0, /*通过次数*/
  `submitted`  INT NOT NULL DEFAULT 0, /*提交次数*/
  PRIMARY KEY(`contest_id`, `problem_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;


DROP TABLE IF EXISTS t_contest_user;
CREATE TABLE t_contest_user (
  /*参与比赛的用户*/
  `contest_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  `user_name` VARCHAR(64) NOT NULL, /*冗余字段*/
  PRIMARY KEY(`contest_id`, `user_id`)
) DEFAULT charset = "utf8"  ENGINE = InnoDB;








DROP TABLE IF EXISTS t_discuss;
CREATE TABLE t_discuss (
  /*
  针对一个题目的讨论
  发起的新话题
  回复某人
  */
  `post_id` INT auto_increment NOT NULL,  /*消息id*/
  /*以下两个ID如果是非回复的消息, 使用默认值0, 在插入到数据库后,
   * 触发器将会把post_id, direct_fid, root_id设置为一致, 表示这个消息是原创的
   */
  `direct_fid` INT NOT NULL DEFAULT 0,/*如消息是回复别的消息,这里设置为原始消息的ID*/
  `root_id` INT NOT NULL DEFAULT 0,/*如消息是回复别的消息,一组评论下的根ID*/
  `type` TINYINT NOT NULL, /*类型, 分别表示题目讨论(0), 比赛讨论(1), 消息发布(2)*/
  `porc_id` INT,/*根据type, 保存题目或者是比赛的ID*/
  `theme` VARCHAR(100) NOT NULL DEFAULT "", /*消息的主题*/
  `title` VARCHAR(100) NOT NULL DEFAULT "", /*标题*/
  `content` text NOT NULL,  /*内容*/
  `post_time` bigint NOT NULL,  /*提交时间*/
  `user_id` INT NOT NULL,  /*提交用户*/
  `user_name`     VARCHAR(64)   NOT NULL,   /*用户名, 冗余字段, 避免联合查询*/
  `reply` INT NOT NULL DEFAULT 0,/*消息被评论的次数*/
  `watch` INT NOT NULL DEFAULT 0, /*消息被查看的次数*/
  `first` TINYINT NOT NULL DEFAULT 0, /*是否置顶*/
  PRIMARY KEY(`post_id`)
) DEFAULT charset = "utf8" auto_increment=1  ENGINE = InnoDB;








DROP TABLE t_language;
CREATE TABLE t_language (
  `language_id` TINYINT auto_increment NOT NULL,
  `language`   VARCHAR(16) NOT NULL,
  PRIMARY KEY(`language_id`, `language`)
)  DEFAULT charset = "utf8" auto_increment=1  ENGINE = InnoDB;

INSERT INTO t_language(`language`) VALUES ("C");
INSERT INTO t_language(`language`) VALUES ("C++");
INSERT INTO t_language(`language`) VALUES ("Java");
INSERT INTO t_language(`language`) VALUES ("Python2");
INSERT INTO t_language(`language`) VALUES ("Python3");




/*视图*/
DROP VIEW IF EXISTS v_submit_record;
CREATE VIEW v_submit_record AS
  SELECT
    submit_id,
    contest_id,
    t_contest.title contest_title,
    problem_id,
    t_problem.title problem_title,
    user_id,
    user_name,
    compile_result,
    result,
    time_consume,
    mem_consume,
    language,
    source_code,
    code_length,
    submit_time,
    judge_time
  FROM ((t_submit_record submit LEFT JOIN t_compile_info compile USING(`submit_id`)) JOIN t_user USING (`user_id`)) JOIN t_problem USING (`problem_id`) LEFT JOIN t_contest USING (`contest_id`);





/*触发器*/
/*删除用户后*/
DROP TRIGGER IF EXISTS deleteUserTrigger;
CREATE TRIGGER deleteUserTrigger AFTER DELETE ON t_user
  FOR EACH ROW
  BEGIN
    SET @userID = OLD.user_id;
    /*删除该该用户的提交记录*/
    DELETE FROM t_submit_record WHERE t_submit_record.user_id=@userID;
    /*删除该用户的讨论记录*/
    DELETE FROM t_discuss WHERE t_discuss.user_id=@userID;
    /*删除该用户的比赛记录*/
    DELETE FROM t_contest_user WHERE t_contest_user.user_id=@userID;
  END;


/*删除提交记录*/
DROP TRIGGER IF EXISTS deleteSubmitRecordTrigger;
CREATE TRIGGER deleteSubmitRecordTrigger AFTER DELETE ON t_submit_record
  FOR EACH ROW
  BEGIN
    SET @submitID = OLD.submit_id;
    /*删除该提交下的详细评测结果*/
    DELETE FROM t_judge_detail WHERE t_judge_detail.submit_id=@submitID;
    /*删除该提交下的编译结果*/
    DELETE FROM t_compile_info WHERE t_compile_info.submit_id=@submitID;
    /*删除该记录下的错误信息*/
    DELETE FROM t_system_error WHERE t_system_error.submit_id=@submitID;
  END;


/*删除题目记录*/
DROP TRIGGER IF EXISTS deleteProblemTrigger;
CREATE TRIGGER deleteProblemTrigger AFTER DELETE ON t_problem
  FOR EACH ROW
  BEGIN
    /*获取被删除提交的ID*/
    SET @problemID = OLD.problem_id;
    /*删除该题目的测试点*/
    DELETE FROM t_test_point WHERE t_test_point.problem_id=@problemID;
    /*删除所有关于该题目的提交记录*/
    DELETE FROM t_submit_record WHERE t_submit_record.problem_id=@problemID;
    /*从所有比赛中删除这个题目*/
    DELETE FROM t_contest_problem WHERE t_contest_problem.problem_id=@problemID;
  END;


/*删除比赛*/
DROP TRIGGER IF EXISTS deleteContestTrgger;
CREATE TRIGGER deleteContestTrgger AFTER DELETE ON t_contest
  FOR EACH ROW
  BEGIN
    /*获取被删除比赛的ID*/
    SET @contestID = OLD.contest_id;
    /*删除比赛用户*/
    DELETE FROM t_contest_user WHERE t_contest_user.contest_id=@contestID;
    /*删除比赛题目*/
    DELETE FROM t_contest_problem WHERE t_contest_problem.contest_id=@contestID;
    /*删除该比赛下的所有提交代码*/
    DELETE FROM t_submit_record WHERE t_submit_record.contest_id=@contestID;
  END;


/*有新的提交后更新提交次数*/
DROP TRIGGER IF EXISTS insertSubmitRecordTrigger;
CREATE TRIGGER insertSubmitRecordTrigger AFTER INSERT ON t_submit_record
  FOR EACH ROW
  BEGIN
    /*更新题目和用户的提交次数*/
    UPDATE t_problem SET submitted=(SELECT count(submit_id) FROM t_submit_record WHERE t_submit_record.problem_id=NEW.problem_id) WHERE problem_id=NEW.problem_id;
    UPDATE t_user    SET submitted=(SELECT count(submit_id) FROM t_submit_record WHERE    t_submit_record.user_id=NEW.user_id)    WHERE    user_id=NEW.user_id;
  END;

/*有正确的提交后更新提交的通过人数和用户的通过题数*/
DROP TRIGGER IF EXISTS updateSubmitRecordTrigger;
CREATE TRIGGER updateSubmitRecordTrigger AFTER UPDATE ON t_submit_record
  FOR EACH ROW
  BEGIN
    IF NEW.result='Accepted' THEN
      UPDATE t_user    SET accepted=(SELECT count(DISTINCT problem_id) FROM t_submit_record WHERE    user_id=NEW.user_id    AND result='Accepted') WHERE user_id=NEW.user_id;
      UPDATE t_problem SET accepted=(SELECT count(DISTINCT user_id)    FROM t_submit_record WHERE problem_id=NEW.problem_id AND result='Accepted') WHERE problem_id=NEW.problem_id;
    END IF;
  END;





/*存储过程*/
/*更新评论的回复次数*/
DROP PROCEDURE IF EXISTS updateReplyCount;
CREATE PROCEDURE updateReplyCount(IN postID INT)
  BEGIN
    DECLARE cnt INT;
    SELECT count(post_id) INTO cnt FROM t_discuss WHERE post_id!=root_id AND post_id!=direct_fid AND root_id=postID;
    UPDATE t_discuss SET reply=cnt WHERE post_id=postID;
  END;





SELECT * FROM (t_contest_problem LEFT JOIN t_contest USING(`contest_id`)) LEFT JOIN t_problem USING (`problem_id`);


/*查询一个题目通过的人数*/
SELECT count(t.user_id) as passed from (SELECT DISTINCT user_id FROM v_submit_record WHERE contest_id=19 AND problem_id=7 AND result='Accepted') AS t;

