/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.17.10_3306
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.17.10:3306
 Source Schema         : yudada

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 11/08/2024 21:52:27
*/
    
-- 创建库
create database if not exists zhida;

-- 切换库
use yudada;

-- ----------------------------
-- Table structure for app
-- ----------------------------
DROP TABLE IF EXISTS `app`;
CREATE TABLE `app`
(
    `id`              bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `appName`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '应用名',
    `appDesc`         varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用描述',
    `appIcon`         varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '应用图标',
    `appType`         tinyint                                                       NOT NULL DEFAULT 0 COMMENT '应用类型（0-得分类，1-测评类）',
    `scoringStrategy` tinyint                                                       NOT NULL DEFAULT 0 COMMENT '评分策略（0-自定义，1-AI）',
    `reviewStatus`    int                                                           NOT NULL DEFAULT 0 COMMENT '审核状态：0-待审核, 1-通过, 2-拒绝',
    `reviewMessage`   varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核信息',
    `reviewerId`      bigint NULL DEFAULT NULL COMMENT '审核人 id',
    `reviewTime`      datetime NULL DEFAULT NULL COMMENT '审核时间',
    `userId`          bigint                                                        NOT NULL COMMENT '创建用户 id',
    `createTime`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`      datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`        tinyint                                                       NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX             `idx_appName`(`appName` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1821197856724246531 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '应用' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app
-- ----------------------------
INSERT INTO `app`
VALUES (1, '自定义MBTI性格测试', '测试性格',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 0, 0, NULL, NULL,
        NULL, 1, '2024-04-24 15:58:05', '2024-08-10 23:35:11', 0);
INSERT INTO `app`
VALUES (2, '自定义得分测试', '测试得分',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 0, 0, 0, NULL, NULL,
        NULL, 1, '2024-04-25 11:39:30', '2024-08-10 23:42:39', 0);
INSERT INTO `app`
VALUES (3, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-07-11 05:27:10', 0);
INSERT INTO `app`
VALUES (4, 'AI 得分测试', '看看你熟悉多少首都',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 0, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:56', '2024-07-11 05:27:10', 0);
INSERT INTO `app`
VALUES (1805961170641772546, 'haha', 'aa',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 0, 1, 1, NULL, NULL,
        NULL, 1, '2024-06-27 05:47:49', '2024-07-11 05:27:10', 0);
INSERT INTO `app`
VALUES (1805961170641772547, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772548, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772549, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772550, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772551, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772552, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772553, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772554, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772555, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772556, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772557, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772558, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772559, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772560, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772561, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772562, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772563, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772564, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772565, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772566, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1805961170641772567, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-04-26 16:38:12', '2024-05-09 15:09:53', 0);
INSERT INTO `app`
VALUES (1820018026016157697, 'asdsadasda', 'xxx', NULL, 0, 0, 1, '', NULL, NULL, 1, '2024-08-05 00:44:45',
        '2024-08-05 00:44:45', 0);
INSERT INTO `app`
VALUES (1821197856724246530, 'MBTI 性格测试', '做题目来测量你的性格，结果为十六种人格中的一种',
        'https://gratisography.com/wp-content/uploads/2023/10/gratisography-cool-cat-1170x780.jpg', 1, 1, 1, NULL, NULL,
        NULL, 1, '2024-08-08 06:52:59', '2024-08-10 23:43:15', 0);

-- ----------------------------
-- Table structure for question
-- ----------------------------
DROP TABLE IF EXISTS `question`;
CREATE TABLE `question`
(
    `id`              bigint   NOT NULL AUTO_INCREMENT COMMENT 'id',
    `questionContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '题目内容（json格式）',
    `appId`           bigint   NOT NULL COMMENT '应用 id',
    `userId`          bigint   NOT NULL COMMENT '创建用户 id',
    `createTime`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`        tinyint  NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX             `idx_appId`(`appId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1805962410754859011 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '题目' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of question
-- ----------------------------
INSERT INTO `question`
VALUES (1,
        '[{\"title\":\"你通常更喜欢\",\"options\":[{\"result\":\"I\",\"score\":0,\"value\":\"独自工作\",\"key\":\"A\"},{\"result\":\"E\",\"score\":0,\"value\":\"与他人合作\",\"key\":\"B\"}]},{\"title\":\"当安排活动时\",\"options\":[{\"result\":\"J\",\"score\":0,\"value\":\"喜欢有明确的计划\",\"key\":\"A\"},{\"result\":\"P\",\"score\":0,\"value\":\"更愿意随机应变\",\"key\":\"B\"}]},{\"title\":\"你如何看待规则\",\"options\":[{\"result\":\"T\",\"score\":0,\"value\":\"认为应该严格遵守\",\"key\":\"A\"},{\"result\":\"F\",\"score\":0,\"value\":\"认为应灵活运用\",\"key\":\"B\"}]},{\"title\":\"在社交场合中\",\"options\":[{\"result\":\"E\",\"score\":0,\"value\":\"经常是说话的人\",\"key\":\"A\"},{\"result\":\"I\",\"score\":0,\"value\":\"更倾向于倾听\",\"key\":\"B\"}]},{\"title\":\"面对新的挑战\",\"options\":[{\"result\":\"J\",\"score\":0,\"value\":\"先研究再行动\",\"key\":\"A\"},{\"result\":\"P\",\"score\":0,\"value\":\"边做边学习\",\"key\":\"B\"}]},{\"title\":\"在日常生活中\",\"options\":[{\"result\":\"S\",\"score\":0,\"value\":\"注重细节和事实\",\"key\":\"A\"},{\"result\":\"N\",\"score\":0,\"value\":\"注重概念和想象\",\"key\":\"B\"}]},{\"title\":\"做决定时\",\"options\":[{\"result\":\"T\",\"score\":0,\"value\":\"更多基于逻辑分析\",\"key\":\"A\"},{\"result\":\"F\",\"score\":0,\"value\":\"更多基于个人情感\",\"key\":\"B\"}]},{\"title\":\"对于日常安排\",\"options\":[{\"result\":\"S\",\"score\":0,\"value\":\"喜欢有结构和常规\",\"key\":\"A\"},{\"result\":\"N\",\"score\":0,\"value\":\"喜欢自由和灵活性\",\"key\":\"B\"}]},{\"title\":\"当遇到问题时\",\"options\":[{\"result\":\"P\",\"score\":0,\"value\":\"首先考虑可能性\",\"key\":\"A\"},{\"result\":\"J\",\"score\":0,\"value\":\"首先考虑后果\",\"key\":\"B\"}]},{\"title\":\"你如何看待时间\",\"options\":[{\"result\":\"T\",\"score\":0,\"value\":\"时间是一种宝贵的资源\",\"key\":\"A\"},{\"result\":\"F\",\"score\":0,\"value\":\"时间是相对灵活的概念\",\"key\":\"B\"}]}]',
        1, 1, '2024-04-24 16:41:53', '2024-07-13 22:30:01', 0);
INSERT INTO `question`
VALUES (2,
        '[{\"options\":[{\"score\":0,\"value\":\"利马\",\"key\":\"A\"},{\"score\":0,\"value\":\"圣多明各\",\"key\":\"B\"},{\"score\":0,\"value\":\"圣萨尔瓦多\",\"key\":\"C\"},{\"score\":1,\"value\":\"波哥大\",\"key\":\"D\"}],\"title\":\"哥伦比亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"蒙特利尔\",\"key\":\"A\"},{\"score\":0,\"value\":\"多伦多\",\"key\":\"B\"},{\"score\":1,\"value\":\"渥太华\",\"key\":\"C\"},{\"score\":0,\"value\":\"温哥华\",\"key\":\"D\"}],\"title\":\"加拿大的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"大阪\",\"key\":\"A\"},{\"score\":1,\"value\":\"东京\",\"key\":\"B\"},{\"score\":0,\"value\":\"京都\",\"key\":\"C\"},{\"score\":0,\"value\":\"名古屋\",\"key\":\"D\"}],\"title\":\"日本的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"墨尔本\",\"key\":\"A\"},{\"score\":0,\"value\":\"悉尼\",\"key\":\"B\"},{\"score\":0,\"value\":\"布里斯班\",\"key\":\"C\"},{\"score\":1,\"value\":\"堪培拉\",\"key\":\"D\"}],\"title\":\"澳大利亚的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"雅加达\",\"key\":\"A\"},{\"score\":0,\"value\":\"曼谷\",\"key\":\"B\"},{\"score\":0,\"value\":\"胡志明市\",\"key\":\"C\"},{\"score\":0,\"value\":\"吉隆坡\",\"key\":\"D\"}],\"title\":\"印度尼西亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"上海\",\"key\":\"A\"},{\"score\":0,\"value\":\"杭州\",\"key\":\"B\"},{\"score\":1,\"value\":\"北京\",\"key\":\"C\"},{\"score\":0,\"value\":\"广州\",\"key\":\"D\"}],\"title\":\"中国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"汉堡\",\"key\":\"A\"},{\"score\":0,\"value\":\"慕尼黑\",\"key\":\"B\"},{\"score\":1,\"value\":\"柏林\",\"key\":\"C\"},{\"score\":0,\"value\":\"科隆\",\"key\":\"D\"}],\"title\":\"德国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"釜山\",\"key\":\"A\"},{\"score\":1,\"value\":\"首尔\",\"key\":\"B\"},{\"score\":0,\"value\":\"大田\",\"key\":\"C\"},{\"score\":0,\"value\":\"仁川\",\"key\":\"D\"}],\"title\":\"韩国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"瓜达拉哈拉\",\"key\":\"A\"},{\"score\":0,\"value\":\"蒙特雷\",\"key\":\"B\"},{\"score\":1,\"value\":\"墨西哥城\",\"key\":\"C\"},{\"score\":0,\"value\":\"坎昆\",\"key\":\"D\"}],\"title\":\"墨西哥的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"开罗\",\"key\":\"A\"},{\"score\":0,\"value\":\"亚历山大\",\"key\":\"B\"},{\"score\":0,\"value\":\"卢克索\",\"key\":\"C\"},{\"score\":0,\"value\":\"卡利乌比亚\",\"key\":\"D\"}],\"title\":\"埃及的首都是?\"}]',
        2, 1, '2024-04-25 15:03:07', '2024-05-09 12:28:58', 0);
INSERT INTO `question`
VALUES (3,
        '[{\"title\":\"你通常更喜欢\",\"options\":[{\"key\":\"A\",\"value\":\"独自工作\",\"result\":\"I\"},{\"key\":\"B\",\"value\":\"与他人合作\",\"result\":\"E\"}]},{\"title\":\"当安排活动时\",\"options\":[{\"key\":\"A\",\"value\":\"喜欢有明确的计划\",\"result\":\"J\"},{\"key\":\"B\",\"value\":\"更愿意随机应变\",\"result\":\"P\"}]},{\"title\":\"你如何看待规则\",\"options\":[{\"key\":\"A\",\"value\":\"认为应该严格遵守\",\"result\":\"T\"},{\"key\":\"B\",\"value\":\"认为应灵活运用\",\"result\":\"F\"}]},{\"title\":\"在社交场合中\",\"options\":[{\"key\":\"A\",\"value\":\"经常是说话的人\",\"result\":\"E\"},{\"key\":\"B\",\"value\":\"更倾向于倾听\",\"result\":\"I\"}]},{\"title\":\"面对新的挑战\",\"options\":[{\"key\":\"A\",\"value\":\"先研究再行动\",\"result\":\"J\"},{\"key\":\"B\",\"value\":\"边做边学习\",\"result\":\"P\"}]},{\"title\":\"在日常生活中\",\"options\":[{\"key\":\"A\",\"value\":\"注重细节和事实\",\"result\":\"S\"},{\"key\":\"B\",\"value\":\"注重概念和想象\",\"result\":\"N\"}]},{\"title\":\"做决定时\",\"options\":[{\"key\":\"A\",\"value\":\"更多基于逻辑分析\",\"result\":\"T\"},{\"key\":\"B\",\"value\":\"更多基于个人情感\",\"result\":\"F\"}]},{\"title\":\"对于日常安排\",\"options\":[{\"key\":\"A\",\"value\":\"喜欢有结构和常规\",\"result\":\"S\"},{\"key\":\"B\",\"value\":\"喜欢自由和灵活性\",\"result\":\"N\"}]},{\"title\":\"当遇到问题时\",\"options\":[{\"key\":\"A\",\"value\":\"首先考虑可能性\",\"result\":\"P\"},{\"key\":\"B\",\"value\":\"首先考虑后果\",\"result\":\"J\"}]},{\"title\":\"你如何看待时间\",\"options\":[{\"key\":\"A\",\"value\":\"时间是一种宝贵的资源\",\"result\":\"T\"},{\"key\":\"B\",\"value\":\"时间是相对灵活的概念\",\"result\":\"F\"}]}]',
        3, 1, '2024-04-26 16:39:29', '2024-08-08 06:18:14', 0);
INSERT INTO `question`
VALUES (4,
        '[{\"options\":[{\"score\":0,\"value\":\"利马\",\"key\":\"A\"},{\"score\":0,\"value\":\"圣多明各\",\"key\":\"B\"},{\"score\":0,\"value\":\"圣萨尔瓦多\",\"key\":\"C\"},{\"score\":1,\"value\":\"波哥大\",\"key\":\"D\"}],\"title\":\"哥伦比亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"蒙特利尔\",\"key\":\"A\"},{\"score\":0,\"value\":\"多伦多\",\"key\":\"B\"},{\"score\":1,\"value\":\"渥太华\",\"key\":\"C\"},{\"score\":0,\"value\":\"温哥华\",\"key\":\"D\"}],\"title\":\"加拿大的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"大阪\",\"key\":\"A\"},{\"score\":1,\"value\":\"东京\",\"key\":\"B\"},{\"score\":0,\"value\":\"京都\",\"key\":\"C\"},{\"score\":0,\"value\":\"名古屋\",\"key\":\"D\"}],\"title\":\"日本的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"墨尔本\",\"key\":\"A\"},{\"score\":0,\"value\":\"悉尼\",\"key\":\"B\"},{\"score\":0,\"value\":\"布里斯班\",\"key\":\"C\"},{\"score\":1,\"value\":\"堪培拉\",\"key\":\"D\"}],\"title\":\"澳大利亚的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"雅加达\",\"key\":\"A\"},{\"score\":0,\"value\":\"曼谷\",\"key\":\"B\"},{\"score\":0,\"value\":\"胡志明市\",\"key\":\"C\"},{\"score\":0,\"value\":\"吉隆坡\",\"key\":\"D\"}],\"title\":\"印度尼西亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"上海\",\"key\":\"A\"},{\"score\":0,\"value\":\"杭州\",\"key\":\"B\"},{\"score\":1,\"value\":\"北京\",\"key\":\"C\"},{\"score\":0,\"value\":\"广州\",\"key\":\"D\"}],\"title\":\"中国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"汉堡\",\"key\":\"A\"},{\"score\":0,\"value\":\"慕尼黑\",\"key\":\"B\"},{\"score\":1,\"value\":\"柏林\",\"key\":\"C\"},{\"score\":0,\"value\":\"科隆\",\"key\":\"D\"}],\"title\":\"德国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"釜山\",\"key\":\"A\"},{\"score\":1,\"value\":\"首尔\",\"key\":\"B\"},{\"score\":0,\"value\":\"大田\",\"key\":\"C\"},{\"score\":0,\"value\":\"仁川\",\"key\":\"D\"}],\"title\":\"韩国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"瓜达拉哈拉\",\"key\":\"A\"},{\"score\":0,\"value\":\"蒙特雷\",\"key\":\"B\"},{\"score\":1,\"value\":\"墨西哥城\",\"key\":\"C\"},{\"score\":0,\"value\":\"坎昆\",\"key\":\"D\"}],\"title\":\"墨西哥的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"开罗\",\"key\":\"A\"},{\"score\":0,\"value\":\"亚历山大\",\"key\":\"B\"},{\"score\":0,\"value\":\"卢克索\",\"key\":\"C\"},{\"score\":0,\"value\":\"卡利乌比亚\",\"key\":\"D\"}],\"title\":\"埃及的首都是?\"}]',
        4, 1, '2024-04-26 16:39:29', '2024-05-09 12:28:58', 0);
INSERT INTO `question`
VALUES (1805962410754859010,
        '[{\"title\":\"\",\"options\":[{\"result\":\"\",\"score\":0,\"value\":\"\",\"key\":\"\"}]}]',
        1805961170641772546, 1, '2024-06-27 05:52:45', '2024-06-27 05:52:45', 0);

-- ----------------------------
-- Table structure for scoring_result
-- ----------------------------
DROP TABLE IF EXISTS `scoring_result`;
CREATE TABLE `scoring_result`
(
    `id`               bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `resultName`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '结果名称，如物流师',
    `resultDesc`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '结果描述',
    `resultPicture`    varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结果图片',
    `resultProp`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结果属性集合 JSON，如 [I,S,T,J]',
    `resultScoreRange` int NULL DEFAULT NULL COMMENT '结果得分范围，如 80，表示 80及以上的分数命中此结果',
    `appId`            bigint                                                        NOT NULL COMMENT '应用 id',
    `userId`           bigint                                                        NOT NULL COMMENT '创建用户 id',
    `createTime`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`       datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`         tinyint                                                       NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX              `idx_appId`(`appId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '评分结果' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of scoring_result
-- ----------------------------
INSERT INTO `scoring_result`
VALUES (1, 'ISTJ（物流师）', '忠诚可靠，被公认为务实，注重细节。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"S\",\"T\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (2, 'ISFJ（守护者）', '善良贴心，以同情心和责任为特点。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"S\",\"F\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (3, 'INFJ（占有者）', '理想主义者，有着深刻的洞察力，善于理解他人。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"N\",\"F\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (4, 'INTJ（设计师）', '独立思考者，善于规划和实现目标，理性而果断。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"N\",\"T\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (5, 'ISTP（运动员）', '冷静自持，善于解决问题，擅长实践技能。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"S\",\"T\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (6, 'ISFP（艺术家）', '具有艺术感和敏感性，珍视个人空间和自由。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"S\",\"F\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (7, 'INFP（治愈者）', '理想主义者，富有创造力，以同情心和理解他人著称。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"N\",\"F\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (8, 'INTP（学者）', '思维清晰，探索精神，独立思考且理性。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"I\",\"N\",\"T\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (9, 'ESTP（拓荒者）', '敢于冒险，乐于冒险，思维敏捷，行动果断。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"S\",\"T\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (10, 'ESFP（表演者）', '热情开朗，善于社交，热爱生活，乐于助人。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"S\",\"F\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (11, 'ENFP（倡导者）', '富有想象力，充满热情，善于激发他人的活力和潜力。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"N\",\"F\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (12, 'ENTP（发明家）', '充满创造力，善于辩论，挑战传统，喜欢探索新领域。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"N\",\"T\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (13, 'ESTJ（主管）', '务实果断，善于组织和管理，重视效率和目标。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"S\",\"T\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (14, 'ESFJ（尽责者）', '友善热心，以协调、耐心和关怀为特点，善于团队合作。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"S\",\"F\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (15, 'ENFJ（教导着）', '热情关爱，善于帮助他人，具有领导力和社交能力。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"N\",\"F\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (16, 'ENTJ（统帅）', '果断自信，具有领导才能，善于规划和执行目标。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg',
        '[\"E\",\"N\",\"T\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (17, '首都知识大师', '你真棒棒哦，首都知识非常出色！',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 9, 2, 1,
        '2024-04-25 15:05:44', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (18, '地理小能手！', '你对于世界各国的首都了解得相当不错，但还有一些小地方需要加强哦！',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 7, 2, 1,
        '2024-04-25 15:05:44', '2024-08-03 22:37:50', 0);
INSERT INTO `scoring_result`
VALUES (19, '继续加油！', '还需努力哦',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 0, 2, 1,
        '2024-04-25 15:05:44', '2024-08-03 22:37:50', 0);

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`           bigint                                                        NOT NULL AUTO_INCREMENT COMMENT 'id',
    `userAccount`  varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
    `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
    `unionId`      varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '微信开放平台id',
    `mpOpenId`     varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '公众号openId',
    `userName`     varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户昵称',
    `userAvatar`   varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
    `userProfile`  varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户简介',
    `userRole`     varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
    `createTime`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`   datetime                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`     tinyint                                                       NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX          `idx_unionId`(`unionId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1819677753604059138 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user`
VALUES (1, 'aixming', 'a1c021d43c899914ea835c3115261414', NULL, NULL, 'AixMing',
        'https://k.sinaimg.cn/n/sinakd20110/560/w1080h1080/20230930/915d-f3d7b580c33632b191e19afa0a858d31.jpg/w700d1q75cms.jpg',
        '欢迎来编程导航学习', 'admin', '2024-05-09 11:13:13', '2024-08-04 02:11:44', 0);
INSERT INTO `user`
VALUES (1819003986229891073, 'commonuser', 'a1c021d43c899914ea835c3115261414', NULL, NULL, NULL, NULL, NULL, 'user',
        '2024-08-02 05:35:19', '2024-08-02 05:35:19', 0);
INSERT INTO `user`
VALUES (1819677753604059137, 'test1', 'a1c021d43c899914ea835c3115261414', NULL, NULL, '一个帅哥',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-cyber-kitty-800x525.jpg', '一个无敌的人',
        'user', '2024-08-04 02:12:38', '2024-08-04 02:13:36', 0);

-- ----------------------------
-- Table structure for user_answer
-- ----------------------------
DROP TABLE IF EXISTS `user_answer`;
CREATE TABLE `user_answer`
(
    `id`              bigint   NOT NULL AUTO_INCREMENT,
    `appId`           bigint   NOT NULL COMMENT '应用 id',
    `appType`         tinyint  NOT NULL DEFAULT 0 COMMENT '应用类型（0-得分类，1-角色测评类）',
    `scoringStrategy` tinyint  NOT NULL DEFAULT 0 COMMENT '评分策略（0-自定义，1-AI）',
    `choices`         text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '用户答案（JSON 数组）',
    `resultId`        bigint NULL DEFAULT NULL COMMENT '评分结果 id',
    `resultName`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结果名称，如物流师',
    `resultDesc`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '结果描述',
    `resultPicture`   varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '结果图标',
    `resultScore`     int NULL DEFAULT NULL COMMENT '得分',
    `userId`          bigint   NOT NULL COMMENT '用户 id',
    `createTime`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime`      datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `isDelete`        tinyint  NOT NULL DEFAULT 0 COMMENT '是否删除',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX             `idx_appId`(`appId` ASC) USING BTREE,
    INDEX             `idx_userId`(`userId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1822189290868703233 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户答题记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_answer
-- ----------------------------
INSERT INTO `user_answer`
VALUES (1, 1, 1, 0, '[\"A\",\"A\",\"A\",\"B\",\"A\",\"A\",\"A\",\"B\",\"B\",\"A\"]', 1, 'ISTJ（物流师）',
        '忠诚可靠，被公认为务实，注重细节。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-05-09 15:08:22', '2024-08-03 22:38:19', 0);
INSERT INTO `user_answer`
VALUES (2, 2, 0, 0, '[\"D\",\"C\",\"B\",\"D\",\"A\",\"C\",\"C\",\"B\",\"C\",\"A\"]', 17, '首都知识大师',
        '你真棒棒哦，首都知识非常出色！',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', 10, 1,
        '2024-05-09 15:08:36', '2024-08-03 22:38:19', 0);
INSERT INTO `user_answer`
VALUES (1812022246647005185, 1, 1, 0, '[\"A\",\"B\",\"A\",\"B\",\"A\",\"B\",\"A\",\"B\",\"A\",\"B\"]', 1,
        'ISTJ（物流师）', '忠诚可靠，被公认为务实，注重细节。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-07-13 23:12:21', '2024-08-03 22:38:19', 0);
INSERT INTO `user_answer`
VALUES (1819621475414441985, 1, 1, 0, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', 7,
        'INFP（治愈者）', '理想主义者，富有创造力，以同情心和理解他人著称。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-08-03 22:29:00', '2024-08-03 22:38:19', 0);
INSERT INTO `user_answer`
VALUES (1819637869006827521, 1, 1, 0, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', 1,
        'ISTJ（物流师）', '忠诚可靠，被公认为务实，注重细节。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-08-03 23:34:09', '2024-08-03 23:34:09', 0);
INSERT INTO `user_answer`
VALUES (1819637952775467010, 1, 1, 0, '[\"A\",\"B\",\"A\",\"B\",\"A\",\"B\",\"A\",\"B\",\"A\",\"B\"]', 1,
        'ISTJ（物流师）', '忠诚可靠，被公认为务实，注重细节。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-08-03 23:34:29', '2024-08-03 23:34:29', 0);
INSERT INTO `user_answer`
VALUES (1819638114201645058, 4, 0, 0, '[\"A\",\"B\",\"C\",\"D\",\"A\",\"B\",\"C\",\"D\",\"A\",\"C\"]', NULL, NULL, NULL,
        NULL, NULL, 1, '2024-08-03 23:35:07', '2024-08-03 23:35:07', 0);
INSERT INTO `user_answer`
VALUES (1819638141800165378, 4, 0, 0, '[\"A\",\"B\",\"C\",\"D\",\"A\",\"B\",\"C\",\"D\",\"A\",\"C\"]', NULL, NULL, NULL,
        NULL, NULL, 1, '2024-08-03 23:35:14', '2024-08-03 23:35:14', 0);
INSERT INTO `user_answer`
VALUES (1819638157100986370, 4, 0, 0, '[\"A\",\"B\",\"C\",\"D\",\"A\",\"B\",\"C\",\"D\",\"A\",\"C\"]', NULL, NULL, NULL,
        NULL, NULL, 1, '2024-08-03 23:35:17', '2024-08-03 23:35:17', 0);
INSERT INTO `user_answer`
VALUES (1819638164638150657, 4, 0, 0, '[\"A\",\"B\",\"C\",\"D\",\"A\",\"B\",\"C\",\"D\",\"A\",\"C\"]', NULL, NULL, NULL,
        NULL, NULL, 1, '2024-08-03 23:35:19', '2024-08-03 23:35:19', 0);
INSERT INTO `user_answer`
VALUES (1819638349267218434, 2, 0, 0, '[\"A\",\"B\",\"C\",\"D\",\"A\",\"B\",\"C\",\"D\",\"A\",\"C\"]', 19, '继续加油！',
        '还需努力哦', 'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', 3,
        1, '2024-08-03 23:36:03', '2024-08-03 23:36:03', 0);
INSERT INTO `user_answer`
VALUES (1819639131412639746, 2, 0, 0, '[\"D\",\"C\",\"B\",\"D\",\"A\",\"C\",\"C\",\"B\",\"C\",\"A\"]', 17,
        '首都知识大师', '你真棒棒哦，首都知识非常出色！',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', 10, 1,
        '2024-08-03 23:39:10', '2024-08-03 23:39:10', 0);
INSERT INTO `user_answer`
VALUES (1820010471835578369, 1, 1, 0, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', 7,
        'INFP（治愈者）', '理想主义者，富有创造力，以同情心和理解他人著称。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-08-05 00:14:44', '2024-08-05 00:14:44', 0);
INSERT INTO `user_answer`
VALUES (1820010600013508609, 2, 0, 0, '[\"D\",\"D\",\"D\",\"D\",\"D\",\"D\",\"D\",\"D\",\"D\",\"D\"]', 19, '继续加油！',
        '还需努力哦', 'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', 2,
        1, '2024-08-05 00:15:15', '2024-08-05 00:15:15', 0);
INSERT INTO `user_answer`
VALUES (1821129487383838722, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你表现出典型的INTJ型性格特征，即内倾、直觉、思考、判断。你倾向于独自工作，喜欢明确的计划，强调规则的重要性，社交场合中较为主动，面对挑战时倾向于先研究后行动，注重细节和事实，做决定时更多基于逻辑分析，偏好有结构和常规的日常生活，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个理性、独立且善于策划的人。',
        NULL, NULL, 1, '2024-08-08 02:21:18', '2024-08-08 02:21:23', 0);
INSERT INTO `user_answer`
VALUES (1821129490428903426, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你被评估为INTJ型性格，也就是内倾、直觉、思考、判断型。你倾向于独自工作，喜欢明确的计划，认为规则应该被严格遵守。在社交场合中，你不是特别活跃，倾向于先研究再行动，面对新的挑战。在日常生活中，你注重细节和事实，做决定时更多基于逻辑分析。你喜欢有结构和常规的日常安排，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点都符合INTJ型性格的特征。',
        NULL, NULL, 1, '2024-08-08 02:21:19', '2024-08-08 02:21:24', 0);
INSERT INTO `user_answer`
VALUES (1821843884351791105, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你表现出典型的INTJ型性格特征，即内倾、直觉、思考、判断。你倾向于独自工作，喜欢明确的计划，强调规则的重要性，社交场合中较为主动，面对挑战时倾向于先研究后行动，注重细节和事实，做决定时更多基于逻辑分析，偏好有结构和常规的日常生活，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个理性、独立且善于策划的人。',
        NULL, NULL, 1, '2024-08-10 01:40:03', '2024-08-10 01:40:10', 0);
INSERT INTO `user_answer`
VALUES (1821843967503867905, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你表现出典型的INTJ型性格特征，即内倾、直觉、思考、判断。你倾向于独自工作，喜欢明确的计划，强调规则的重要性，社交场合中较为主动，面对挑战时倾向于先研究后行动，注重细节和事实，做决定时更多基于逻辑分析，偏好有结构和常规的日常生活，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个理性、独立且善于策划的人。',
        NULL, NULL, 1, '2024-08-10 01:40:23', '2024-08-10 01:40:23', 0);
INSERT INTO `user_answer`
VALUES (1821844005604925441, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你表现出典型的INTJ型性格特征，即内倾、直觉、思考、判断。你倾向于独自工作，喜欢明确的计划，强调规则的重要性，社交场合中较为主动，面对挑战时倾向于先研究后行动，注重细节和事实，做决定时更多基于逻辑分析，偏好有结构和常规的日常生活，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个理性、独立且善于策划的人。',
        NULL, NULL, 1, '2024-08-10 01:40:32', '2024-08-10 01:40:32', 0);
INSERT INTO `user_answer`
VALUES (1821844041424281602, 1, 1, 1, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', NULL,
        '灵活的协作者',
        '根据你的回答，你倾向于是一个喜欢与他人合作、能够随机应变的人。你对规则持有灵活的态度，社交场合中更愿意倾听他人，面对挑战时采取边做边学的策略。在日常生活中，你注重概念和想象，做决定时更多基于个人情感，偏好自由和灵活的日常安排。遇到问题时，你首先考虑后果，并且将时间视为相对灵活的概念。这些特质表明你是一个适应性强、善于协作的个体。',
        NULL, NULL, 1, '2024-08-10 01:40:40', '2024-08-10 01:40:45', 0);
INSERT INTO `user_answer`
VALUES (1821845375498166274, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你表现出典型的INTJ型性格特征，即内倾、直觉、思考、判断。你倾向于独自工作，喜欢明确的计划，强调规则的重要性，社交场合中较为主动，面对挑战时倾向于先研究后行动，注重细节和事实，做决定时更多基于逻辑分析，偏好有结构和常规的日常生活，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个理性、独立且善于策划的人。',
        NULL, NULL, 1, '2024-08-10 01:45:59', '2024-08-10 01:45:59', 0);
INSERT INTO `user_answer`
VALUES (1821845413574057986, 1, 1, 1, '[\"A\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', NULL,
        '内向直觉型思考者',
        '根据你的回答，你倾向于独自工作，喜欢随机应变的安排活动，认为规则应灵活运用，社交场合中更倾向于倾听，面对新挑战时采取边做边学的态度。你注重概念和想象，做决定时更多基于个人情感，追求自由和灵活性，面对问题时首先考虑后果，将时间视为相对灵活的概念。这些特点表明你是一个内向直觉型思考者，具有独立思考、灵活应变和深度分析的能力。',
        NULL, NULL, 1, '2024-08-10 01:46:08', '2024-08-10 01:46:12', 0);
INSERT INTO `user_answer`
VALUES (1821858590571466754, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你表现出典型的INTJ型性格特征，即内倾、直觉、思考、判断。你倾向于独自工作，喜欢明确的计划，强调规则的重要性，社交场合中较为主动，面对挑战时倾向于先研究后行动，注重细节和事实，做决定时更多基于逻辑分析，偏好有结构和常规的日常生活，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个理性、独立且善于策划的人。',
        NULL, NULL, 1, '2024-08-10 02:38:29', '2024-08-10 02:38:35', 0);
INSERT INTO `user_answer`
VALUES (1821858654614294529, 1, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ型性格',
        '根据你的回答，你表现出典型的INTJ型性格特征，即内倾、直觉、思考、判断。你倾向于独自工作，喜欢明确的计划，强调规则的重要性，社交场合中较为主动，面对挑战时倾向于先研究后行动，注重细节和事实，做决定时更多基于逻辑分析，偏好有结构和常规的日常生活，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个理性、独立且善于策划的人。',
        NULL, NULL, 1, '2024-08-10 02:38:45', '2024-08-10 02:38:45', 0);
INSERT INTO `user_answer`
VALUES (1822173950122131456, 1, 1, 1, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"A\",\"A\",\"A\"]', NULL,
        '全面合作型',
        '根据你的回答，你倾向于与他人合作，能够灵活应对变化，认为规则应该根据实际情况灵活应用。在社交场合中，你更愿意倾听他人，面对挑战时采取实践学习方法。你注重概念和想象力，在做决定时倾向于基于个人情感。虽然你喜欢有结构和常规的生活安排，但在遇到问题时，你首先考虑的是各种可能性，并且你非常珍惜时间，将其视为宝贵的资源。综合来看，你是一个全面合作型的人，能够在团队中发挥积极作用，同时保持个人特色和灵活性。',
        NULL, NULL, 1, '2024-08-10 23:33:39', '2024-08-10 23:33:44', 0);
INSERT INTO `user_answer`
VALUES (1822174626801778688, 1, 1, 1, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', NULL,
        '灵活的协作者',
        '根据你的回答，你倾向于是一个喜欢与他人合作、能够随机应变的人。你对规则持有灵活的态度，社交场合中更愿意倾听他人，面对挑战时采取边做边学的策略。在日常生活中，你注重概念和想象，做决定时更多基于个人情感，偏好自由和灵活的日常安排。遇到问题时，你首先考虑后果，并且将时间视为相对灵活的概念。这些特点表明你是一个性格灵活、善于协作的个体。',
        NULL, NULL, 1, '2024-08-10 23:34:33', '2024-08-10 23:34:38', 0);
INSERT INTO `user_answer`
VALUES (1822174864077750272, 1, 1, 0, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', 1,
        'ISTJ（物流师）', '忠诚可靠，被公认为务实，注重细节。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-08-10 23:35:28', '2024-08-10 23:35:28', 0);
INSERT INTO `user_answer`
VALUES (1822174943111020544, 1, 1, 0, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', 7,
        'INFP（治愈者）', '理想主义者，富有创造力，以同情心和理解他人著称。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-08-10 23:35:50', '2024-08-10 23:35:50', 0);
INSERT INTO `user_answer`
VALUES (1822176489668345856, 1, 1, 0, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', 7,
        'INFP（治愈者）', '理想主义者，富有创造力，以同情心和理解他人著称。',
        'https://gratisography.com/wp-content/uploads/2024/01/gratisography-reindeer-dog-800x525.jpg', NULL, 1,
        '2024-08-10 23:41:53', '2024-08-10 23:41:53', 0);
INSERT INTO `user_answer`
VALUES (1822179615544705024, 3, 1, 1, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', NULL,
        'ENFP - 热情随性的倡导者',
        '根据你的回答，你表现出明显的ENFP性格特征。你喜欢与他人合作，愿意随机应变，认为规则应灵活运用，这表明你是一个外向、直觉型、情感型的人。在社交场合中，你倾向于倾听，面对挑战时边做边学，注重概念和想象，做决定时更多基于个人情感，这都体现了你的随性和热情。你喜欢自由和灵活的日常安排，面对问题时首先考虑后果，对时间的看法也体现了灵活性。总的来说，你的回答符合ENFP型人格的特点，你是一个充满热情、富有创意、善于沟通的倡导者。',
        NULL, NULL, 1, '2024-08-10 23:54:17', '2024-08-10 23:54:24', 0);
INSERT INTO `user_answer`
VALUES (1822179782385729536, 3, 1, 1, '[\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', NULL,
        'ENFP - 热情随性的倡导者',
        '根据你的回答，你表现出明显的ENFP性格特征。你喜欢与他人合作，愿意随机应变，认为规则应灵活运用，这表明你是一个外向、直觉型、情感型的人。在社交场合中，你倾向于倾听，面对挑战时边做边学，注重概念和想象，做决定时更多基于个人情感，这都体现了你的随性和热情。你喜欢自由和灵活的日常安排，面对问题时首先考虑后果，对时间的看法也体现了灵活性。总的来说，你的回答符合ENFP型人格的特点，你是一个充满热情、富有创意、善于沟通的倡导者。',
        NULL, NULL, 1, '2024-08-10 23:54:56', '2024-08-10 23:54:56', 0);
INSERT INTO `user_answer`
VALUES (1822179828518879232, 3, 1, 1, '[\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\",\"A\"]', NULL,
        'INTJ - 策划者',
        '根据你的回答，你被归类为INTJ型，也就是策划者。你倾向于独自工作，喜欢明确的计划，严格遵守规则，并在社交场合中经常是说话的人。你面对新挑战时会先研究再行动，注重细节和事实，做决定时更多基于逻辑分析。你偏好有结构和常规的日常安排，遇到问题时首先考虑可能性，并且认为时间是宝贵的资源。这些特点表明你是一个独立、条理清晰、善于策划和高度理性的人。',
        NULL, NULL, 1, '2024-08-10 23:55:09', '2024-08-10 23:55:14', 0);
INSERT INTO `user_answer`
VALUES (1822189290868703232, 3, 1, 1, '[\"A\",\"B\",\"A\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\",\"B\"]', NULL,
        '内向直觉思考感知型（INTP）',
        '根据你的回答，你倾向于独自工作，愿意随机应变，认为规则应该被严格遵守，且在社交场合中更倾向于倾听。你面对新挑战时采取边做边学的态度，注重概念和想象，在做决定时更多基于个人情感。你喜欢日常安排中有自由和灵活性，面对问题时首先考虑后果，且你认为时间是相对灵活的概念。这些特点表明你的MBTI性格为内向直觉思考感知型（INTP），你是一个具有创新精神、逻辑性强、善于分析的人。',
        NULL, NULL, 1, '2024-08-11 00:32:42', '2024-08-11 00:32:47', 0);

SET FOREIGN_KEY_CHECKS = 1;
