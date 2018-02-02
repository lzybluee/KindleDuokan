import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	static String[] list = { "凡尔纳全集", "圣经", "斯蒂芬金经典系列", "The Lord of the Rings - J. R. R. Tolkien", "银河英雄传说", "追忆似水年华",
			"尼尔盖曼作品集", "从惊讶到思考", "美国种族简史", "莎士比亚全集", "韩寒作品集", "侠客行（下）",
			"The Hobbit_ Or There and Back Again - J. R. R. Tolkien", "那些路过心上的经典-民国大师经典书系", "小王子", "物理世界奇遇记", "暮光之城",
			"侠客行（上）", "连城诀", "原来这才是春秋", "雪山飞狐", "Sherlock Holmes_ The Complete Illustrate - Sir Arthur Conan Doyle",
			"说文解字注(上下）", "The Children of Hurin - J. R. R. Tolkien", "碧血剑（上）", "天龙八部（四）", "倚天屠龙记（一）", "鹿鼎记（一）", "魔戒",
			"书剑恩仇录（上）", "天龙八部（五）", "倚天屠龙记（四）", "鹿鼎记（五）", "老友记剧本", "鹿鼎记（三）", "M Is for Magic - Neil Gaiman", "鹿鼎记（四）",
			"书剑恩仇录（下）", "倚天屠龙记（三）", "鹿鼎记（二）", "笑傲江湖（二）", "笑傲江湖（一）", "神雕侠侣（一）", "天龙八部（一）", "神雕侠侣（二）", "神雕侠侣（四）",
			"神雕侠侣（三）", "倚天屠龙记（二）", "天龙八部（二）", "飞狐外传（上）", "飞狐外传（下）", "笑傲江湖（三）", "笑傲江湖（四）", "碧血剑（下）", "天龙八部（三）",
			"射雕英雄传（四）", "射雕英雄传（二）", "射雕英雄传（一）", "射雕英雄传（三）", "第三帝国的兴亡", "经济学原理",
			"A Storm of Swords - George R. R. Martin", "安徒生童话", "罗马帝国衰亡史", "近距离看美国", "基督山伯爵", "The American Language",
			"科学史及其与哲学和宗教", "Atlas Shrugged - Ayn Rand", "历史是个什么玩意儿", "1Q84",
			"A Dance With Dragons - George R. R. Martin", "村上春树合集", "A Feast for Crows - George R. R. Martin", "全球通史",
			"Merriam-Webster's Guide to Punctuation and Style", "A Clash of Kings - George R. R. Martin", "丹布朗作品集",
			"武林外史", "The Legend of Sigurd and Gudrun - J. R. R. Tolkien", "生存手册", "Edge of Eternity - Ken Follett",
			"与神对话", "Coraline - Neil Gaiman", "荷马史诗 伊利亚特", "世界电影史", "A Game Of Thrones - George R. R. Martin", "沙丘三部曲",
			"七种武器", "甲骨文", "论美国的民主", "失落的秘符", "韩寒五年文集", "谣言粉碎机", "Fall of Giants - Ken Follett", "猜想与反驳", "历史的天空",
			"Winter of the World - Ken Follett", "大数据时代", "Harry Potter and the Order of the Phoenix - J. K. Rowling",
			"The Lost Symbol - Dan Brown", "物种起源", "生命中不能承受之轻", "云图", "Ulysses - James Joyce", "第二十二条军规", "日本短篇推理小说选",
			"昨日的世界", "魔鬼出没的世界", "洛丽塔", "影响美国宪政的25个司法大案", "简·爱", "Verbal Advantage", "偶发空缺", "消费者行为学",
			"Harry Potter and the Goblet Of Fire - J. K. Rowling", "海边的卡夫卡",
			"Harry Potter and the Deathly Hallows - J. K. Rowling", "最后的贵族", "日本史", "苏菲的世界", "巴黎圣母院",
			"Harry Potter and the Half-Blood Prince - J. K. Rowling", "我不是教你诈", "人件", "真相与自白",
			"Merriam-Webster's Vocabulary Builder",
			"Lovecraft's Fiction Volume I, 1905-1925 - Howard Phillips Lovecraft", "DOOM启示录", "吴大师讲西游", "旧制度与大革命",
			"The Da Vinci Code - Dan Brown", "The Hitchhiker's Guide to the Galaxy - Douglas Adams", "百年孤独", "曾国藩家书",
			"生活中的经济学", "霍乱时期的爱情", "Angels & Demons - Dan Brown",
			"Harry Potter and the Prisoner of Azkaban - J. K. Rowling", "理想国", "美国习惯用语", "American Gods - Neil Gaiman",
			"Lovecraft's Fiction Volume III, 1928-193 - Howard Phillips Lovecraft", "Deception Point - Dan Brown",
			"挪威的森林", "论中国", "Lovecraft's Fiction Volume IV, 1932-1936 - Howard Phillips Lovecraft",
			"Lovecraft's Fiction Volume II, 1926-1928 - Howard Phillips Lovecraft", "A Child's History of England",
			"人在欧洲", "围城", "Harry Potter and the Sorcerer's Stone - J. K. Rowling", "歌舞伎町案内人", "少年Pi的奇幻漂流", "百战奇略",
			"狂热分子", "禅者的初心", "城堡", "Harry Potter and the Chamber of Secrets - J. K. Rowling", "西线无战事",
			"The Silmarillion - J. R. R. Tolkien", "球状闪电", "犹太智慧枕边书", "拖延心理学", "可怕的对称", "GOTH断掌事件", "你一生的故事", "历史深处的忧虑",
			"货币战争", "一个人的电影", "Good Omens_ The Nice and Accurate Prophe - Neil Gaiman", "月亮和六便士", "神经浪游者", "在路上", "新世界",
			"西游日记", "中国建筑史", "十个词汇里的中国", "新东方词根词缀大全", "Neverwhere_ A Novel - Neil Gaiman", "时间旅行者的妻子", "中国人应知的国学常识",
			"At the mountains of madness - H. P. Lovecraft", "学会提问", "令人战栗的格林童话", "富爸爸穷爸爸", "经济学的思维方式", "一口气读完世界历史",
			"The Tales of Beedle the Bard - J. K. Rowling", "别跟我说你懂日本", "1988我想和这个世界谈谈", "易中天先秦诸子百家争鸣",
			"The Restaurant at the End of the Univers - Douglas Adams", "万物简史", "写在人生边上", "Stardust - Neil Gaiman",
			"麦田里的守望者", "这本书叫什么", "麻美姐姐教你", "天安门", "中国历代政治得失", "易中天品三国", "好妈妈胜过好老师", "了不起的盖茨比", "无人生还", "定西孤儿院纪事",
			"乡关何处", "沙僧日记", "活着", "胡林的子女", "王尔德童话", "Mostly harmless - Douglas Adams",
			"So Long, and Thanks for All the Fish - Douglas Adams", "Life, the Universe and Everything - Douglas Adams",
			"经典常谈", "当彩色的声音尝起来是甜的", "拆掉思维里的墙", "货币战争3", "动物庄园", "The Great Gatsby - F. Scott Fitzgerald", "情人", "城南旧事",
			"生命是什么", "货币战争2", "Quidditch Through the Ages - J. K. Rowling", "吉檀迦利", "金庸识小录", "蔡康永的说话之道", "万物有灵且美",
			"每天懂一点色彩心理学", "狭义与广义相对论浅说", "乔布斯的魔力演讲", "当世界年纪还小的时候", "老人与海", "欧亨利短篇小说集", "The Elements of Style",
			"趁生命气息逗留", "冰与火之歌（全15册）", "福尔摩斯探案全集（午夜文库·全九册）", "丘吉尔世界大战丛书（全17册）", "第一推动丛书·宇宙系列（共6册）",
			"霍金经典三部曲（时间简史｜果壳中的宇宙｜大设计 全三册）", "王小波全集（全十卷）", "明朝那些事儿（全七册）", "精灵宝钻", "第一推动丛书·综合系列（共五册）", "第一推动丛书·物理系列（共7册）",
			"清醒思考的艺术", "大秦帝国（全新修订进阶版 全18册）", "马伯庸精选作品集（共11册）", "安珀志（全10册）", "怪诞行为学（共三册）", "龙枪传奇（全三册）", "地海传奇六部曲（共六册）",
			"哈利·波特完整系列（共七册）", "龙枪编年史（全三册）", "美国文明史", "经典音乐故事", "枪炮、病菌与钢铁（修订版）", "白鹿原", "信号与噪声", "走进费曼丛书（共6册）",
			"海伯利安四部曲", "KK三部曲（失控+科技想要什么+必然）", "努门诺尔与中洲之未完的传说", "纳尼亚传奇（全三册）", "道金斯科学经典系列（自私的基因|盲眼钟表匠|魔鬼的牧师 共三册）",
			"上帝掷骰子吗：量子物理史话", "小岛经济学", "三体（全三册）", "银河帝国（1-7）：基地系列七部曲", "银河帝国（8-12）：机器人系列五部曲", "极简欧洲史", "崩溃：社会如何选择成败兴亡",
			"动荡的世界", "英伦魔法拾遗", "阿瑟·黑利系列（共六册）", "明智行动的艺术", "中国通史（插图本）", "阿瑟·克拉克经典科幻超级套装（共四册）", "蝴蝶效应之谜：走近分形与混沌", "蜀山剑侠传",
			"三行情书", "通俗天文学：和大师一起与宇宙对话", "霍比特人", "银河帝国（13-15）：帝国系列三部曲", "我们的信任：为什么有时信任，有时不信任", "黑客与画家", "美国中学是这样的",
			"未来简史：从智人到智神", "格拉德威尔经典系列（异类｜眨眼之间｜引爆点｜逆转｜大开眼界 共五册）", "霍布斯鲍姆年代四部曲（全四册）", "中国古代奇技淫巧", "“民主”的奇形怪状", "七堂极简物理课",
			"图灵的秘密", "拆掉思维里的墙", "我是个算命先生（全四册）", "星空的琴弦：天文学史话", "厨房里的哲学家", "MacTalk·人生元编程", "史蒂夫·乔布斯传（修订版）",
			"人类简史：从动物到上帝", "民主的模式（最新修订版）", "批判性思维（原书第10版）", "黑客（机械工业出版社）", "安·兰德作品集（共五册）", "英伦魔法师（全三卷）", "越读越有趣的经济学",
			"银河系搭车客指南（五部曲套装）", "相约星期二（米奇·阿尔博姆作品）", "安德的游戏三部曲（全三册）", "狼图腾：纪念版", "费马大定理：一个困惑了世间智者358年的谜",
			"硅谷禁书（青少年励志经典文库）", "鲁迅全集", "弯曲的旅行", "鬼吹灯（全集）", "病毒来袭", "魔戒 3：王者归来", "基因社会", "黑骏马（马爱农译版）", "哈扎尔辞典（阴本）",
			"肯·福莱特经典：世界的凛冬（共3册）", "中国民间故事", "当李晓峰成为SKY", "电影作者：101位世界杰出导演", "世界哲学史", "知觉之门", "第三种黑猩猩（贾雷德·戴蒙德作品）",
			"何伟三部曲（套装共3册）", "哈扎尔辞典（阳本珍藏版）", "狄更斯讲英国史（全三册）", "陈舜臣十八史略（共五册）", "史上最强日本史（全三册）", "纪伯伦散文集", "美国，真的和你想的不一样",
			"英语人名词语趣谈", "盗墓笔记（全集）", "音乐符号", "史迈利三部曲（全三册）", "百万英镑（经典译林）", "时间的形状：相对论史话", "相对论百问", "克苏鲁神话", "爱与数学",
			"学会提问", "拥抱逝水年华（阿兰·德波顿文集）", "Fantastic Beasts and Where to Find Them: The Original Screenplay",
			"追风筝的人系列（全三册）", "童话大王讲经典系列（西游记｜三国演义｜红楼梦｜水浒传 全四册）", "光明王系列（全二册）", "茶事遍路", "肯·福莱特经典：永恒的边缘（共3册）",
			"揭示宇宙奥秘的13个常数", "中国哲学简史", "自控力（共二册）", "美国文明三部曲（全三册）", "十亿美金的教训", "集装箱改变世界（修订版）", "第三种黑猩猩：人类的身世与未来（睿文馆）",
			"教育经典：卡尔·威特的教育全书", "人类群星闪耀时（译林名著精选）", "魔戒 1：魔戒同盟", "中国人的修养（中国长安出版社）", "量子之谜：物理学遇到意识", "沙丘", "魔戒 2：双塔殊途",
			"气味：秘密的诱惑者", "销售圣经（白金修订版）", "哲学家们都干了些什么？", "活学活用博弈论", "尤利西斯（经典译林）", "赤裸裸的统计学", "飘（上）", "谁动了我的奶酪",
			"肯·福莱特经典：巨人的陨落（共3册）", "经度：一个孤独的天才解决所处时代最大难题的真实故事", "飘（下）", "若星汉天空", "季羡林私人史", "放学后的侦探队", "肠子，脑子，厨子",
			"万历十五年（经典版）", "白宫密谈", "微宇宙的上帝", "盛夏的方程式", "长尾理论（信经典系列）", "周汝昌梦解红楼", "神秘的量子生命", "思考，快与慢", "梦的解析（中华书局典藏本）",
			"嫌疑人X的献身", "新课标读本：鲁滨逊漂流记", "我是个年轻人 我心情不太好", "哲学家们都干了些什么（全新修订版）", "英语人人说：非学不可的口语王",
			"Hogwarts: An Incomplete and Unreliable Guide", "荒野侦探", "呼啸山庄（译林版）", "证明与反驳：数学发现的逻辑", "一指流沙，我们都握不住的那段年华",
			"2666", "广告大师奥格威：未公诸于世的选集（修订版）", "神经漫游者三部曲（全三册）", "反乌托邦三部曲（套装共三册）", "教养的秘密：希利尔0～6岁儿童黄金训练法", "万有引力之虹", "巨婴国",
			"中信国学大典：庄子", "塞尔伯恩博物志", "异类：不一样的成功启示录（新版）", "西游记", "国富论（全译本）", "牛津通识读本：考古学的过去与未来（中文版）", "古典音乐一本通", "贪婪的大脑",
			"风沙星辰", "大家丛书：金庸传", "不可不知的世界5000年灾难记录", "新课标必读丛书：格林童话", "中国现代文学史（第三版）", "推好看：十位推理大师的私房代表作", "莱茵河的囚徒",
			"中日恩怨两千年", "水浒传", "红楼梦", "拖延心理学：向与生俱来的行为顽症宣战", "错误的正确：世界经典趣味博弈", "龙文身的女孩", "奥丁的儿女", "希区柯克悬念故事全集",
			"中国科幻小说经典", "第一夜", "第一日", "新课标必读丛书：莎士比亚悲剧喜剧集", "白夜行",
			"Short Stories from Hogwarts of Power, Politics and Pesky Poltergeists", "中外法学名著指要", "大清灭亡启示录（1894—1911）",
			"历代经济变革得失", "胡适谈哲学与人生", "阿西莫夫：永恒的终结", "世界奇幻小说简史：巨龙的颂歌", "美国众神（同名美剧）", "失乐园（渡边淳一作品）",
			"故事：材质、结构、风格和银幕剧作的原理（果麦经典）", "世界科幻小说简史：星空的旋律", "何伟，再见涪陵（中国故事）", "北大心理课", "爱丽丝镜中奇遇记", "敬文东解密鲁迅", "众病之王：癌症传",
			"沙僧日记全集", "物种起源（新世界出版社）", "霍格沃茨不完全不靠谱指南",
			"Harry Potter and the Cursed Child-Parts One and Two（Special Rehearsal Edition）", "罗伯特议事规则", "三国演义",
			"布谷鸟的呼唤（J.K.罗琳作品）", "寻一个不受人惑的人（胡适文丛）", "生命最后的读书会", "人生的乐趣", "心醉神怡：心理咨询的秘密", "偷影子的人",
			"Short Stories from Hogwarts of Heroism, Hardship and Dangerous Hobbies", "小明经济学", "华杉讲透《孙子兵法》",
			"希腊古典神话（译林版）", "梅奥住院医生成长手记", "天使，望故乡", "从你的全世界路过（电影《摆渡人》典藏版）", "长恨歌（王安忆作品）", "想当厨子的生物学家是个好黑客",
			"最好的抉择：关于看病就医你要知道的常识", "尤利西斯导读", "鬼吹灯之牧野诡事", "你应该记住的拉丁语1234句", "夹边沟记事", "潜伏在办公室", "霍格沃茨：力量·政治与恶作剧幽灵（短篇故事集）",
			"KGB克格勃全传", "剑桥简明金庸武侠史", "阿西莫夫：神们自己", "程序员跳槽全攻略", "刻意练习：如何从新手到大师", "霍格沃茨：勇气·磨难与危险嗜好（短篇故事集）", "解忧杂货店",
			"诗性的寻找", "大教堂与集市", "朋克刑警的骄傲（午夜文库）", "公正：该如何做是好？", "瓦尔登湖（经典译林）", "莎士比亚十四行诗（英汉对照）", "世界推理小说简史：谋杀的魅影",
			"纯粹理性批判", "人类灭绝", "罗生门（世界文学名著典藏）", "悲剧的诞生（译林人文精选）", "阴兽（午夜文库）", "沙丘 2：沙丘救世主", "对话死囚", "幸福的方法",
			"献给阿尔吉侬的花束（世界顶级科幻大师杰作选）", "西方哲学简史", "钱商", "我们台湾这些年", "犹太人教子心经", "简·爱", "玩火的女孩", "莎士比亚故事集", "剑桥倚天屠龙史",
			"菊与刀（壹力文库049）", "海明威作品系列：老人与海", "恶意", "交锋三十年", "你对了，世界就对了", "斯坦福极简经济学", "人生有何意义", "一头想要被吃掉的猪",
			"我们为什么离正义越来越远", "人性的优点（汉英对照）", "不疯魔，不哲学", "罗马神话（经典译林）", "新课标必读丛书：名人传", "美丽英文：那一年，我们一起毕业", "人性的弱点（无删改全译本）",
			"日本简史", "新月集·飞鸟集", "郁达夫散文全集", "为奴十二年", "罪恶生涯（J.K.罗琳作品）", "如何阅读一本书", "我们为什么会分手", "最好的告别：关于衰老与死亡，你必须知道的常识",
			"当下的力量", "蚕（J.K.罗琳作品）", "医生的精进：从仁心仁术到追求卓越", "医生的修炼：在不完美中探索行医的真相", "林语堂文集：人生不过如此", "美丽英文：那些改变未来的身影",
			"一个真实的奥巴马", "悟空传", "请你安静些，好吗？", "哈利·波特与被诅咒的孩子（第一部和第二部）", "安徒生童话", "战争论", "屠夫十字镇", "牢墙内的巴勒斯坦", "此生未完成",
			"忆往述怀", "胡适文选：演讲与时论", "美丽英文：童话若有张不老的脸", "天才在左 疯子在右", "北京法源寺", "生命是什么（里程碑式的科普经典）", "美国牧歌（译林版）",
			"美丽英文：无法忘却的电影对白", "简单的逻辑学", "乡愁（余光中诗精编）", "当呼吸化为空气", "太阳照常升起", "湘行散记", "安静：内向性格的竞争力", "贾平凹散文", "常识", "斯通纳",
			"看懂财经新闻的第一本书", "我们台湾这些年 2", "疾病的隐喻（中英双语版桑塔格文集）", "最美好的人间十日", "美丽英文：一个人，也能有好时光", "娱乐至死", "神奇动物在哪里（原创电影剧本）",
			"美丽英文：那些震撼世界的声音", "乌合之众", "当我们谈论爱情时我们在谈论什么", "民国的角落", "遇见最美的宋词", "岛上书店", "杀死一只知更鸟（纪念版）", "假如给我三天光明：海伦·凯勒自传",
			"新课标同步课外阅读：伊索寓言", "新课标读本：绿野仙踪", "致加西亚的信", "摆渡人", "世界上的另一个你", "我所理解的生活", "有限与无限的游戏：一个哲学家眼中的竞技世界（东西文库）",
			"经典超译本：道德情操论", "城南旧事", "美丽英文：青春是华丽的旅行", "北大哲学课（时代书局版）", "小毛驴与我", "美丽英文：世界上最感人的书信", "中国游记",
			"细胞生命的礼赞（刘易斯·托马斯作品集）", "美丽英文：别处的风景", "美丽英文：爱是最美丽的语言", "翻译漫谈：怎样翻译更地道", "平面国：及正方形的多维世界历险记（早期科幻经典系列）", "人间词话",
			"这书能让你戒烟", "美丽英文：成长是不可替代的事", "动物农庄", "美丽英文：童年是孤单的冒险", "老人与海", "查令十字街84号（珍藏版）", "浮生六记", };

	static String readUrl(String http) throws Exception {
		String content = "";
		try {
			URL url = new URL(http);
			URLConnection URLconnection = url.openConnection();
			URLconnection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			URLconnection.setConnectTimeout(60000);
			URLconnection.setReadTimeout(60000);
			HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream in = httpConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(in);
				BufferedReader bufr = new BufferedReader(isr);
				String str;
				while ((str = bufr.readLine()) != null) {
					content += str + "\n";
				}
				bufr.close();
			} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
				return "";
			} else if (responseCode == HttpURLConnection.HTTP_UNAVAILABLE) {
				return null;
			} else {
				System.out.println("Error " + responseCode + " : " + url);
			}
		} catch (Exception e) {
			return null;
		}
		return content;

	}

	public static void main(String[] args) throws Exception {
		for (String title : list) {
			System.out.print(title);
			String content = readUrl("https://www.douban.com/search?cat=1001&q=" + title.replaceAll(" ", "+"));
			Pattern p = Pattern.compile("<span>\\[.*>(.*?) </a>");
			Matcher m = p.matcher(content);
			if(m.find() && !m.group(1).equals(title)) {
				System.out.print(" => " + m.group(1));
			}
			p = Pattern.compile("<span class=\"rating_nums\">(.*?)</span>");
			m = p.matcher(content);
			if (m.find()) {
				System.out.print(" -> " + m.group(1));
				p = Pattern.compile("<span>\\((\\d+)人评价\\)</span>");
				m = p.matcher(content);
				if(m.find()) {
					System.out.print(" (" + m.group(1) + ")");
				}
			} else {
				System.out.print(" -> N/A");
			}
			System.out.println();
		}
	}

}
