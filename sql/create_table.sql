# 数据库初始化
# @author <a href="https://github.com/liyupi">程序员鱼皮</a>
# @from <a href="https://yupi.icu">编程导航知识星球</a>

-- 创建库
create database if not exists zhida;

-- 切换库
use zhida;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    unionId      varchar(256)                           null comment '微信开放平台id',
    mpOpenId     varchar(256)                           null comment '公众号openId',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userProfile  varchar(512)                           null comment '用户简介',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_unionId (unionId)
) comment '用户' collate = utf8mb4_unicode_ci;

-- 应用表
create table if not exists app
(
    id              bigint auto_increment comment 'id' primary key,
    appName         varchar(128)                       not null comment '应用名',
    appDesc         varchar(2048)                      null comment '应用描述',
    appIcon         varchar(1024)                      null comment '应用图标',
    appType         tinyint  default 0                 not null comment '应用类型（0-得分类，1-测评类）',
    scoringStrategy tinyint  default 0                 not null comment '评分策略（0-自定义，1-AI）',
    thumbNum        int      default 0                 not null comment '点赞数',
    reviewStatus    int      default 0                 not null comment '审核状态：0-待审核, 1-通过, 2-拒绝',
    reviewMessage   varchar(512)                       null comment '审核信息',
    reviewerId      bigint                             null comment '审核人 id',
    reviewTime      datetime                           null comment '审核时间',
    userId          bigint                             not null comment '创建用户 id',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    index idx_appName (appName)
) comment '应用' collate = utf8mb4_unicode_ci;

alter table app
    add thumbNum int default 0 not null comment '点赞数' after scoringStrategy;

-- 点赞表（硬删除）
use zhida;
create table if not exists app_thumb
(
    id         bigint auto_increment comment 'id' primary key,
    appId      bigint                             not null comment '应用 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default current_timestamp not null comment '创建时间',
    updateTime datetime default current_timestamp not null on update current_timestamp comment '更新时间',
    index idx_appId (appId),
    index idx_userId (userId)
) comment '应用点赞表' collate = utf8mb4_unicode_ci;

-- 选择题题目表
create table if not exists question
(
    id              bigint auto_increment comment 'id' primary key,
    questionContent text                               null comment '题目内容（json格式）',
    appId           bigint                             not null comment '应用 id',
    userId          bigint                             not null comment '创建用户 id',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    index idx_appId (appId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 代码题目表
create table if not exists code_question
(
    id          bigint auto_increment comment 'id' primary key,
    title       varchar(512)                       null comment '标题',
    content     text                               null comment '内容',
    tags        varchar(1024)                      null comment '标签列表（json 数组）',
    answer      text                               null comment '题目答案',
    submitNum   int      default 0                 not null comment '题目提交数',
    acceptedNum int      default 0                 not null comment '题目通过数',
    judgeCase   text                               null comment '判题用例（json 数组）',
    judgeConfig text                               null comment '判题配置（json 对象）',
    thumbNum    int      default 0                 not null comment '点赞数',
    favourNum   int      default 0                 not null comment '收藏数',
    userId      bigint                             not null comment '创建用户 id',
    createTime  datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime  datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete    tinyint  default 0                 not null comment '是否删除',
    index idx_userId (userId)
) comment '题目' collate = utf8mb4_unicode_ci;

-- 题目提交表
create table if not exists code_question_submit
(
    id         bigint auto_increment comment 'id' primary key,
    language   varchar(128)                       not null comment '编程语言',
    code       text                               not null comment '用户代码',
    judgeInfo  text                               null comment '判题信息（json 对象）',
    status     int      default 0                 not null comment '判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）',
    questionId bigint                             not null comment '题目 id',
    userId     bigint                             not null comment '创建用户 id',
    createTime datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete   tinyint  default 0                 not null comment '是否删除',
    index idx_questionId (questionId),
    index idx_userId (userId)
) comment '题目提交' collate = utf8mb4_unicode_ci;

-- 评分结果表
create table if not exists scoring_result
(
    id               bigint auto_increment comment 'id' primary key,
    resultName       varchar(128)                       not null comment '结果名称，如物流师',
    resultDesc       text                               null comment '结果描述',
    resultPicture    varchar(1024)                      null comment '结果图片',
    resultProp       varchar(128)                       null comment '结果属性集合 JSON，如 [I,S,T,J]',
    resultScoreRange int                                null comment '结果得分范围，如 80，表示 80及以上的分数命中此结果',
    appId            bigint                             not null comment '应用 id',
    userId           bigint                             not null comment '创建用户 id',
    createTime       datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime       datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete         tinyint  default 0                 not null comment '是否删除',
    index idx_appId (appId)
) comment '评分结果' collate = utf8mb4_unicode_ci;

-- 用户答题记录表
create table if not exists user_answer
(
    id              bigint auto_increment primary key,
    appId           bigint                             not null comment '应用 id',
    appType         tinyint  default 0                 not null comment '应用类型（0-得分类，1-角色测评类）',
    scoringStrategy tinyint  default 0                 not null comment '评分策略（0-自定义，1-AI）',
    choices         text                               null comment '用户答案（JSON 数组）',
    resultId        bigint                             null comment '评分结果 id',
    resultName      varchar(128)                       null comment '结果名称，如物流师',
    resultDesc      text                               null comment '结果描述',
    resultPicture   varchar(1024)                      null comment '结果图标',
    resultScore     int                                null comment '得分',
    userId          bigint                             not null comment '用户 id',
    createTime      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete        tinyint  default 0                 not null comment '是否删除',
    index idx_appId (appId),
    index idx_userId (userId)
) comment '用户答题记录' collate = utf8mb4_unicode_ci;

INSERT INTO `app`
VALUES (1, '自定义MBTI性格测试', '测试性格',
        'https://zhida-1322694943.cos.ap-guangzhou.myqcloud.com/app_picture/1/D72cVdeK-kunkun.png', 1, 0, 0, 1, NULL,
        NULL, NULL, 1, '2024-04-24 15:58:05', '2025-05-30 10:47:28', 0),
       (2, '自定义得分测试', '测试得分',
        'https://zhida-1322694943.cos.ap-guangzhou.myqcloud.com/app_picture/1/D72cVdeK-kunkun.png', 0, 0, 0, 1, NULL,
        NULL, NULL, 1, '2024-04-25 11:39:30', '2025-05-30 10:48:46', 0),
       (3, 'AI MBTI 性格测试', '快来测测你的 MBTI',
        'https://zhida-1322694943.cos.ap-guangzhou.myqcloud.com/app_picture/1/D72cVdeK-kunkun.png', 1, 1, 0, 1, NULL,
        NULL, NULL, 1, '2024-04-26 16:38:12', '2025-05-30 10:48:45', 0),
       (4, 'AI 得分测试', '看看你熟悉多少首都',
        'https://zhida-1322694943.cos.ap-guangzhou.myqcloud.com/app_picture/1/D72cVdeK-kunkun.png', 0, 1, 0, 1, NULL,
        NULL, NULL, 1, '2024-04-26 16:38:56', '2025-05-30 10:46:53', 0);

INSERT INTO `code_question`
VALUES (1922190399259058178, '求两个数的和', '第一行输入两个数 a 和 b，输出它们的和\n\n1 2\n\n3',
        '[\"java\",\"数据结构\"]', 'a = 1，b = 2\n\n输出：3', 0, 0,
        '[{\"input\":\"1 2\",\"output\":\"3\"},{\"input\":\"11 21\",\"output\":\"33\"}]',
        '{\"timeLimit\":1000,\"memoryLimit\":1000,\"stackLimit\":1000}', 0, 0, 1, '2025-05-13 15:21:18',
        '2025-05-13 15:21:18', 0);

INSERT INTO `code_question_submit`
VALUES (1925807307564826625, 'java',
        'import java.util.Scanner;\r\n\r\n/**\r\n * @author AixMing\r\n * @since 2025-05-23 14:39:58\r\n */\r\npublic class Main {\r\n    public static void main(String[] args) {\r\n        Scanner scanner = new Scanner(System.in);\r\n        int a = scanner.nextInt();\r\n        int b = scanner.nextInt();\r\n        System.out.println(a + b);\r\n        scanner.close();\r\n    }\r\n}',
        NULL, 0, 1922190399259058178, 1, '2025-05-23 14:53:36', '2025-05-23 14:53:36', 0),
       (1925811294703726593, 'java',
        'import java.util.Scanner;\r\n\r\n/**\r\n* @author AixMing\r\n* @since 2025-05-23 14:39:58\r\n*/\r\npublic class Main {\r\n    public static void main(String[] args) {\r\n        Scanner scanner = new Scanner(System.in);\r\n        int a = scanner.nextInt();\r\n        int b = scanner.nextInt();\r\n        System.out.println(a + b);\r\n        scanner.close();\r\n    }\r\n}',
        NULL, 0, 1922190399259058178, 1, '2025-05-23 15:09:27', '2025-05-23 15:09:27', 0),
       (1925811860976709633, 'java',
        'import java.util.Scanner;\r\n\r\n/**\r\n* @author AixMing\r\n* @since 2025-05-23 14:39:58\r\n*/\r\npublic class Main {\r\n    public static void main(String[] args) {\r\n        Scanner scanner = new Scanner(System.in);\r\n        int a = scanner.nextInt();\r\n        int b = scanner.nextInt();\r\n        System.out.println(a + b);\r\n        scanner.close();\r\n    }\r\n}',
        NULL, 0, 1922190399259058178, 1, '2025-05-23 15:11:42', '2025-05-23 15:11:42', 0),
       (1925812157644095489, 'java',
        'import java.util.Scanner;\r\n\r\n/**\r\n* @author AixMing\r\n* @since 2025-05-23 14:39:58\r\n*/\r\npublic class Main {\r\n    public static void main(String[] args) {\r\n        Scanner scanner = new Scanner(System.in);\r\n        int a = scanner.nextInt();\r\n        int b = scanner.nextInt();\r\n        System.out.println(a + b);\r\n        scanner.close();\r\n    }\r\n}',
        '{\"message\":\"评价结果：\\n1. 代码正确：用户提交的代码能够正确读取两个整数输入，并输出它们的和，符合算法题目要求。\\n2. 符合测试用例要求：提交的代码能够处理给定的测试用例，对于输入1 2输出3，输入11 21输出33，均能正确执行。\\n3. 时间和空间复杂度符合要求：该代码的时间复杂度为O(1)，因为它只执行了固定次数的操作，不依赖于输入大小。空间复杂度也为O(1)，因为它使用了固定数量的变量。代码没有使用额外的数据结构，因此空间使用符合要求。\"}',
        2, 1922190399259058178, 1, '2025-05-23 15:12:52', '2025-05-23 15:13:01', 0),
       (1925813042805817345, 'java',
        'import java.util.Scanner;\r\n\r\n/**\r\n* @author AixMing\r\n* @since 2025-05-23 14:39:58\r\n*/\r\npublic class Main {\r\n    public static void main(String[] args) {\r\n        Scanner scanner = new Scanner(System.in);\r\n        int a = scanner.nextInt();\r\n        int b = scanner.nextInt();\r\n        System.out.println(a + b);\r\n        scanner.close();\r\n    }\r\n}',
        '{\"message\":\"评价结果：\\n1. 代码正确：用户提交的代码能够正确读取两个整数并输出它们的和，符合算法题目要求。\\n2. 符合测试用例要求：提交的代码能够处理给定的测试用例，正确输出预期结果。\\n3. 时间和空间复杂度符合要求：该代码的时间复杂度为O(1)，因为它只执行了固定次数的操作，不依赖于输入大小。空间复杂度也为O(1)，因为它使用了固定数量的变量。代码没有使用额外的数据结构，因此空间使用符合要求。\"}',
        2, 1922190399259058178, 1, '2025-05-23 15:16:23', '2025-05-23 15:17:09', 0),
       (1925813818898894850, 'java',
        'import java.util.Scanner;\r\n\r\n/**\r\n* @author AixMing\r\n* @since 2025-05-23 14:39:58\r\n*/\r\npublic class Main {\r\n    public static void main(String[] args) {\r\n        Scanner scanner = new Scanner(System.in);\r\n        int a = scanner.nextInt();\r\n        int b = scanner.nextInt();\r\n        System.out.println(a + b);\r\n        scanner.close();\r\n    }\r\n}',
        '{\"message\":\"评价结果：\\n1. 代码正确：用户提交的代码能够正确读取两个整数并输出它们的和，符合算法题目要求。\\n2. 符合测试用例要求：提交的代码能够处理给定的测试用例，正确输出预期结果。\\n3. 时间和空间复杂度符合要求：该代码的时间复杂度为O(1)，因为它只执行了固定次数的操作，不依赖于输入大小。空间复杂度也为O(1)，因为它使用了固定数量的变量。代码没有使用额外的数据结构，因此空间使用符合要求。\"}',
        2, 1922190399259058178, 1, '2025-05-23 15:19:28', '2025-05-23 15:19:32', 0),
       (1925814330218106882, 'java',
        'import java.util.Scanner;\r\n\r\n/**\r\n* @author AixMing\r\n* @since 2025-05-23 14:39:58\r\n*/\r\npublic class Main {\r\n    public static void main(String[] args) {\r\n        Scanner scanner = new Scanner(System.in);\r\n        int a = scanner.nextInt();\r\n        int b = scanner.nextInt();\r\n        System.out.println(a + b);\r\n        scanner.close();\r\n    }\r\n}',
        '{\"message\":\"评价结果：\\n1. 代码正确：用户提交的代码能够正确读取两个整数并输出它们的和，符合算法题目要求。\\n2. 符合测试用例要求：提交的代码能够处理给定的测试用例，正确输出预期结果。\\n3. 时间和空间复杂度符合要求：该代码的时间复杂度为O(1)，因为它只执行了固定次数的操作，不依赖于输入大小。空间复杂度也为O(1)，因为它使用了固定数量的变量。代码没有使用额外的数据结构，因此空间使用符合要求。\"}',
        2, 1922190399259058178, 1, '2025-05-23 15:21:30', '2025-05-23 15:21:34', 0);

INSERT INTO `question`
VALUES (1,
        '[{\"options\":[{\"result\":\"I\",\"value\":\"独自工作\",\"key\":\"A\"},{\"result\":\"E\",\"value\":\"与他人合作\",\"key\":\"B\"}],\"title\":\"1. 你通常更喜欢\"},{\"options\":[{\"result\":\"J\",\"value\":\"喜欢有明确的计划\",\"key\":\"A\"},{\"result\":\"P\",\"value\":\"更愿意随机应变\",\"key\":\"B\"}],\"title\":\"2. 当安排活动时\"},{\"options\":[{\"result\":\"T\",\"value\":\"认为应该严格遵守\",\"key\":\"A\"},{\"result\":\"F\",\"value\":\"认为应灵活运用\",\"key\":\"B\"}],\"title\":\"3. 你如何看待规则\"},{\"options\":[{\"result\":\"E\",\"value\":\"经常是说话的人\",\"key\":\"A\"},{\"result\":\"I\",\"value\":\"更倾向于倾听\",\"key\":\"B\"}],\"title\":\"4. 在社交场合中\"},{\"options\":[{\"result\":\"J\",\"value\":\"先研究再行动\",\"key\":\"A\"},{\"result\":\"P\",\"value\":\"边做边学习\",\"key\":\"B\"}],\"title\":\"5. 面对新的挑战\"},{\"options\":[{\"result\":\"S\",\"value\":\"注重细节和事实\",\"key\":\"A\"},{\"result\":\"N\",\"value\":\"注重概念和想象\",\"key\":\"B\"}],\"title\":\"6. 在日常生活中\"},{\"options\":[{\"result\":\"T\",\"value\":\"更多基于逻辑分析\",\"key\":\"A\"},{\"result\":\"F\",\"value\":\"更多基于个人情感\",\"key\":\"B\"}],\"title\":\"7. 做决定时\"},{\"options\":[{\"result\":\"S\",\"value\":\"喜欢有结构和常规\",\"key\":\"A\"},{\"result\":\"N\",\"value\":\"喜欢自由和灵活性\",\"key\":\"B\"}],\"title\":\"8. 对于日常安排\"},{\"options\":[{\"result\":\"P\",\"value\":\"首先考虑可能性\",\"key\":\"A\"},{\"result\":\"J\",\"value\":\"首先考虑后果\",\"key\":\"B\"}],\"title\":\"9. 当遇到问题时\"},{\"options\":[{\"result\":\"T\",\"value\":\"时间是一种宝贵的资源\",\"key\":\"A\"},{\"result\":\"F\",\"value\":\"时间是相对灵活的概念\",\"key\":\"B\"}],\"title\":\"10. 你如何看待时间\"}]',
        1, 1, '2024-04-24 16:41:53', '2024-05-09 12:28:58', 0),
       (2,
        '[{\"options\":[{\"score\":0,\"value\":\"利马\",\"key\":\"A\"},{\"score\":0,\"value\":\"圣多明各\",\"key\":\"B\"},{\"score\":0,\"value\":\"圣萨尔瓦多\",\"key\":\"C\"},{\"score\":1,\"value\":\"波哥大\",\"key\":\"D\"}],\"title\":\"哥伦比亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"蒙特利尔\",\"key\":\"A\"},{\"score\":0,\"value\":\"多伦多\",\"key\":\"B\"},{\"score\":1,\"value\":\"渥太华\",\"key\":\"C\"},{\"score\":0,\"value\":\"温哥华\",\"key\":\"D\"}],\"title\":\"加拿大的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"大阪\",\"key\":\"A\"},{\"score\":1,\"value\":\"东京\",\"key\":\"B\"},{\"score\":0,\"value\":\"京都\",\"key\":\"C\"},{\"score\":0,\"value\":\"名古屋\",\"key\":\"D\"}],\"title\":\"日本的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"墨尔本\",\"key\":\"A\"},{\"score\":0,\"value\":\"悉尼\",\"key\":\"B\"},{\"score\":0,\"value\":\"布里斯班\",\"key\":\"C\"},{\"score\":1,\"value\":\"堪培拉\",\"key\":\"D\"}],\"title\":\"澳大利亚的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"雅加达\",\"key\":\"A\"},{\"score\":0,\"value\":\"曼谷\",\"key\":\"B\"},{\"score\":0,\"value\":\"胡志明市\",\"key\":\"C\"},{\"score\":0,\"value\":\"吉隆坡\",\"key\":\"D\"}],\"title\":\"印度尼西亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"上海\",\"key\":\"A\"},{\"score\":0,\"value\":\"杭州\",\"key\":\"B\"},{\"score\":1,\"value\":\"北京\",\"key\":\"C\"},{\"score\":0,\"value\":\"广州\",\"key\":\"D\"}],\"title\":\"中国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"汉堡\",\"key\":\"A\"},{\"score\":0,\"value\":\"慕尼黑\",\"key\":\"B\"},{\"score\":1,\"value\":\"柏林\",\"key\":\"C\"},{\"score\":0,\"value\":\"科隆\",\"key\":\"D\"}],\"title\":\"德国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"釜山\",\"key\":\"A\"},{\"score\":1,\"value\":\"首尔\",\"key\":\"B\"},{\"score\":0,\"value\":\"大田\",\"key\":\"C\"},{\"score\":0,\"value\":\"仁川\",\"key\":\"D\"}],\"title\":\"韩国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"瓜达拉哈拉\",\"key\":\"A\"},{\"score\":0,\"value\":\"蒙特雷\",\"key\":\"B\"},{\"score\":1,\"value\":\"墨西哥城\",\"key\":\"C\"},{\"score\":0,\"value\":\"坎昆\",\"key\":\"D\"}],\"title\":\"墨西哥的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"开罗\",\"key\":\"A\"},{\"score\":0,\"value\":\"亚历山大\",\"key\":\"B\"},{\"score\":0,\"value\":\"卢克索\",\"key\":\"C\"},{\"score\":0,\"value\":\"卡利乌比亚\",\"key\":\"D\"}],\"title\":\"埃及的首都是?\"}]',
        2, 1, '2024-04-25 15:03:07', '2024-05-09 12:28:58', 0),
       (3,
        '[{\"options\":[{\"result\":\"I\",\"value\":\"独自工作\",\"key\":\"A\"},{\"result\":\"E\",\"value\":\"与他人合作\",\"key\":\"B\"}],\"title\":\"1. 你通常更喜欢\"},{\"options\":[{\"result\":\"J\",\"value\":\"喜欢有明确的计划\",\"key\":\"A\"},{\"result\":\"P\",\"value\":\"更愿意随机应变\",\"key\":\"B\"}],\"title\":\"2. 当安排活动时\"},{\"options\":[{\"result\":\"T\",\"value\":\"认为应该严格遵守\",\"key\":\"A\"},{\"result\":\"F\",\"value\":\"认为应灵活运用\",\"key\":\"B\"}],\"title\":\"3. 你如何看待规则\"},{\"options\":[{\"result\":\"E\",\"value\":\"经常是说话的人\",\"key\":\"A\"},{\"result\":\"I\",\"value\":\"更倾向于倾听\",\"key\":\"B\"}],\"title\":\"4. 在社交场合中\"},{\"options\":[{\"result\":\"J\",\"value\":\"先研究再行动\",\"key\":\"A\"},{\"result\":\"P\",\"value\":\"边做边学习\",\"key\":\"B\"}],\"title\":\"5. 面对新的挑战\"},{\"options\":[{\"result\":\"S\",\"value\":\"注重细节和事实\",\"key\":\"A\"},{\"result\":\"N\",\"value\":\"注重概念和想象\",\"key\":\"B\"}],\"title\":\"6. 在日常生活中\"},{\"options\":[{\"result\":\"T\",\"value\":\"更多基于逻辑分析\",\"key\":\"A\"},{\"result\":\"F\",\"value\":\"更多基于个人情感\",\"key\":\"B\"}],\"title\":\"7. 做决定时\"},{\"options\":[{\"result\":\"S\",\"value\":\"喜欢有结构和常规\",\"key\":\"A\"},{\"result\":\"N\",\"value\":\"喜欢自由和灵活性\",\"key\":\"B\"}],\"title\":\"8. 对于日常安排\"},{\"options\":[{\"result\":\"P\",\"value\":\"首先考虑可能性\",\"key\":\"A\"},{\"result\":\"J\",\"value\":\"首先考虑后果\",\"key\":\"B\"}],\"title\":\"9. 当遇到问题时\"},{\"options\":[{\"result\":\"T\",\"value\":\"时间是一种宝贵的资源\",\"key\":\"A\"},{\"result\":\"F\",\"value\":\"时间是相对灵活的概念\",\"key\":\"B\"}],\"title\":\"10. 你如何看待时间\"}]',
        3, 1, '2024-04-26 16:39:29', '2024-05-09 12:28:58', 0),
       (4,
        '[{\"options\":[{\"score\":0,\"value\":\"利马\",\"key\":\"A\"},{\"score\":0,\"value\":\"圣多明各\",\"key\":\"B\"},{\"score\":0,\"value\":\"圣萨尔瓦多\",\"key\":\"C\"},{\"score\":1,\"value\":\"波哥大\",\"key\":\"D\"}],\"title\":\"哥伦比亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"蒙特利尔\",\"key\":\"A\"},{\"score\":0,\"value\":\"多伦多\",\"key\":\"B\"},{\"score\":1,\"value\":\"渥太华\",\"key\":\"C\"},{\"score\":0,\"value\":\"温哥华\",\"key\":\"D\"}],\"title\":\"加拿大的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"大阪\",\"key\":\"A\"},{\"score\":1,\"value\":\"东京\",\"key\":\"B\"},{\"score\":0,\"value\":\"京都\",\"key\":\"C\"},{\"score\":0,\"value\":\"名古屋\",\"key\":\"D\"}],\"title\":\"日本的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"墨尔本\",\"key\":\"A\"},{\"score\":0,\"value\":\"悉尼\",\"key\":\"B\"},{\"score\":0,\"value\":\"布里斯班\",\"key\":\"C\"},{\"score\":1,\"value\":\"堪培拉\",\"key\":\"D\"}],\"title\":\"澳大利亚的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"雅加达\",\"key\":\"A\"},{\"score\":0,\"value\":\"曼谷\",\"key\":\"B\"},{\"score\":0,\"value\":\"胡志明市\",\"key\":\"C\"},{\"score\":0,\"value\":\"吉隆坡\",\"key\":\"D\"}],\"title\":\"印度尼西亚的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"上海\",\"key\":\"A\"},{\"score\":0,\"value\":\"杭州\",\"key\":\"B\"},{\"score\":1,\"value\":\"北京\",\"key\":\"C\"},{\"score\":0,\"value\":\"广州\",\"key\":\"D\"}],\"title\":\"中国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"汉堡\",\"key\":\"A\"},{\"score\":0,\"value\":\"慕尼黑\",\"key\":\"B\"},{\"score\":1,\"value\":\"柏林\",\"key\":\"C\"},{\"score\":0,\"value\":\"科隆\",\"key\":\"D\"}],\"title\":\"德国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"釜山\",\"key\":\"A\"},{\"score\":1,\"value\":\"首尔\",\"key\":\"B\"},{\"score\":0,\"value\":\"大田\",\"key\":\"C\"},{\"score\":0,\"value\":\"仁川\",\"key\":\"D\"}],\"title\":\"韩国的首都是?\"},{\"options\":[{\"score\":0,\"value\":\"瓜达拉哈拉\",\"key\":\"A\"},{\"score\":0,\"value\":\"蒙特雷\",\"key\":\"B\"},{\"score\":1,\"value\":\"墨西哥城\",\"key\":\"C\"},{\"score\":0,\"value\":\"坎昆\",\"key\":\"D\"}],\"title\":\"墨西哥的首都是?\"},{\"options\":[{\"score\":1,\"value\":\"开罗\",\"key\":\"A\"},{\"score\":0,\"value\":\"亚历山大\",\"key\":\"B\"},{\"score\":0,\"value\":\"卢克索\",\"key\":\"C\"},{\"score\":0,\"value\":\"卡利乌比亚\",\"key\":\"D\"}],\"title\":\"埃及的首都是?\"}]',
        4, 1, '2024-04-26 16:39:29', '2024-05-09 12:28:58', 0);

INSERT INTO `scoring_result`
VALUES (1, 'ISTJ（物流师）', '忠诚可靠，被公认为务实，注重细节。', 'icon_url_istj', '[\"I\",\"S\",\"T\",\"J\"]', NULL, 1, 1,
        '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (2, 'ISFJ（守护者）', '善良贴心，以同情心和责任为特点。', 'icon_url_isfj', '[\"I\",\"S\",\"F\",\"J\"]', NULL, 1, 1,
        '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (3, 'INFJ（占有者）', '理想主义者，有着深刻的洞察力，善于理解他人。', 'icon_url_infj', '[\"I\",\"N\",\"F\",\"J\"]',
        NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (4, 'INTJ（设计师）', '独立思考者，善于规划和实现目标，理性而果断。', 'icon_url_intj', '[\"I\",\"N\",\"T\",\"J\"]',
        NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (5, 'ISTP（运动员）', '冷静自持，善于解决问题，擅长实践技能。', 'icon_url_istp', '[\"I\",\"S\",\"T\",\"P\"]', NULL, 1,
        1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (6, 'ISFP（艺术家）', '具有艺术感和敏感性，珍视个人空间和自由。', 'icon_url_isfp', '[\"I\",\"S\",\"F\",\"P\"]', NULL,
        1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (7, 'INFP（治愈者）', '理想主义者，富有创造力，以同情心和理解他人著称。', 'icon_url_infp',
        '[\"I\",\"N\",\"F\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (8, 'INTP（学者）', '思维清晰，探索精神，独立思考且理性。', 'icon_url_intp', '[\"I\",\"N\",\"T\",\"P\"]', NULL, 1, 1,
        '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (9, 'ESTP（拓荒者）', '敢于冒险，乐于冒险，思维敏捷，行动果断。', 'icon_url_estp', '[\"E\",\"S\",\"T\",\"P\"]', NULL,
        1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (10, 'ESFP（表演者）', '热情开朗，善于社交，热爱生活，乐于助人。', 'icon_url_esfp', '[\"E\",\"S\",\"F\",\"P\"]', NULL,
        1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (11, 'ENFP（倡导者）', '富有想象力，充满热情，善于激发他人的活力和潜力。', 'icon_url_enfp',
        '[\"E\",\"N\",\"F\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (12, 'ENTP（发明家）', '充满创造力，善于辩论，挑战传统，喜欢探索新领域。', 'icon_url_entp',
        '[\"E\",\"N\",\"T\",\"P\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (13, 'ESTJ（主管）', '务实果断，善于组织和管理，重视效率和目标。', 'icon_url_estj', '[\"E\",\"S\",\"T\",\"J\"]', NULL,
        1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (14, 'ESFJ（尽责者）', '友善热心，以协调、耐心和关怀为特点，善于团队合作。', 'icon_url_esfj',
        '[\"E\",\"S\",\"F\",\"J\"]', NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (15, 'ENFJ（教导着）', '热情关爱，善于帮助他人，具有领导力和社交能力。', 'icon_url_enfj', '[\"E\",\"N\",\"F\",\"J\"]',
        NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (16, 'ENTJ（统帅）', '果断自信，具有领导才能，善于规划和执行目标。', 'icon_url_entj', '[\"E\",\"N\",\"T\",\"J\"]',
        NULL, 1, 1, '2024-04-24 16:57:02', '2024-05-09 12:28:21', 0),
       (17, '首都知识大师', '你真棒棒哦，首都知识非常出色！', NULL, NULL, 9, 2, 1, '2024-04-25 15:05:44',
        '2024-05-09 12:28:21', 0),
       (18, '地理小能手！', '你对于世界各国的首都了解得相当不错，但还有一些小地方需要加强哦！', NULL, NULL, 7, 2, 1,
        '2024-04-25 15:05:44', '2024-05-09 12:28:21', 0),
       (19, '继续加油！', '还需努力哦', NULL, NULL, 0, 2, 1, '2024-04-25 15:05:44', '2024-05-09 12:28:21', 0);

INSERT INTO `user`
VALUES (1, 'aixming', 'a1c021d43c899914ea835c3115261414', NULL, NULL, 'AixMing',
        'https://zhida-1322694943.cos.ap-guangzhou.myqcloud.com/user_avatar/1/FIXVA3wp-kunkun.png',
        '一名无敌年轻有为的全栈工程师', 'admin', '2024-05-09 11:13:13', '2025-05-13 14:52:47', 0),
       (2, 'visitor', 'a1c021d43c899914ea835c3115261414', NULL, NULL, 'visitor',
        'https://zhida-1322694943.cos.ap-guangzhou.myqcloud.com/user_avatar/1/FIXVA3wp-kunkun.png', '普通访客', 'user',
        '2024-05-09 11:13:13', '2025-05-13 14:52:47', 0);

INSERT INTO `user_answer`
VALUES (1, 1, 1, 0, '[\"A\",\"A\",\"A\",\"B\",\"A\",\"A\",\"A\",\"B\",\"B\",\"A\"]', 1, 'ISTJ（物流师）',
        '忠诚可靠，被公认为务实，注重细节。', 'icon_url_istj', NULL, 1, '2024-05-09 15:08:22', '2024-05-09 15:10:13', 0),
       (2, 2, 0, 0, '[\"D\",\"C\",\"B\",\"D\",\"A\",\"C\",\"C\",\"B\",\"C\",\"A\"]', 17, '首都知识大师',
        '你真棒棒哦，首都知识非常出色！', NULL, 10, 1, '2024-05-09 15:08:36', '2024-05-09 15:10:13', 0);

create table if not exists vip_order
(
    id         bigint auto_increment primary key comment '订单号',
    userId     bigint                             not null comment '下单用户 id',
    status     int                                not null default 0 comment '订单状态（0：待支付，1：已支付，2：已完成，3：已取消，4：退款中，5：已退款，6：已关闭）',
    payTime    datetime comment '支付时间',
    refundTime datetime comment '退款时间',
    cancelTime datetime comment '取消时间',
    createTime datetime default current_timestamp not null comment '下单时间',
    updateTime datetime default current_timestamp not null on update current_timestamp comment '更新时间'
) comment 'vip 订单' collate = utf8mb4_unicode_ci;