import net.deechael.khl.api.Channel;
import net.deechael.khl.api.Game;
import net.deechael.khl.api.User;
import net.deechael.khl.bot.KaiheilaBot;
import net.deechael.khl.bot.KaiheilaBotBuilder;
import net.deechael.khl.command.Command;
import net.deechael.khl.command.CommandSender;
import net.deechael.khl.command.KaiheilaCommandBuilder;
import net.deechael.khl.command.argument.UserArgumentType;
import net.deechael.khl.configuration.file.FileConfiguration;
import net.deechael.khl.configuration.file.YamlConfiguration;
import net.deechael.khl.event.EventHandler;
import net.deechael.khl.event.Listener;
import net.deechael.khl.event.channel.UpdateMessageEvent;
import net.deechael.khl.message.Message;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TestBot {
    public static void main(String[] args) {
        // 非必须，只是为了防止意外泄露机器人的token
        // 配置文件API来源于BukkitAPI
       // FileConfiguration configuration = YamlConfiguration.loadConfiguration(new File("config.yml"));
        String apiToken = "1/MTIzMjY=/F2OPkgYNxkqrfmQ5OIVyow==";
        //String apiToken = configuration.getString("");

        // 创建一个新机器人实例
        KaiheilaBot bot = (KaiheilaBot) KaiheilaBotBuilder.builder()
                .createDefault(apiToken) // 使用默认配置构建 KaiheilaBot 实例
                .build(); // 创建实例

        // 注册事件监听器
        bot.addEventListener(new UserEventHandler());

        // 指令API，使用Mojang开发的brigadier库
        // 创建一个名为khljava的指令
      //  KaiheilaCommandBuilder command = Command.create("khljava").literal();
        KaiheilaCommandBuilder command = Command.create("test") .literal();
        // 当用户仅输入“.khljava”使调用该方法
        command.executes(context -> {
            CommandSender sender = context.getSource(); // 获取命令发送者
            Channel channel = sender.getChannel(); // 获取发送频道
            User user = sender.getUser(); //获取发送用户
            channel.sendTempMessage("你调用了khljava指令！", user, false); // 向用户发送一条临时消息（仅该用户可见）
            return 1; // 如果返回0则表示失败，返回其他大于0的数字表示成功
        });
        // 定参指令，如用户输入“.khljava test”则会调用该方法
        command.then(Command.create("test").literal().executes(context -> {
            CommandSender sender = context.getSource(); // 获取命令发送者
            Channel channel = sender.getChannel(); // 获取发送频道
            User user = sender.getUser(); //获取发送用户
            channel.sendTempMessage("你调用了khljava test指令！", user, false); // 向用户发送一条临时消息（仅该用户可见）
            channel.sendMessage("Ur my father.",false) ;
            return 1; // 如果返回0则表示失败，返回其他大于0的数字表示成功
        }));

        command.then(Command.create("晖哥").literal().executes(context -> {
            CommandSender sender = context.getSource(); // 获取命令发送者
            Channel channel = sender.getChannel(); // 获取发送频道
            User user = sender.getUser(); //获取发送用户
            channel.sendTempMessage("你调用了khljava test指令！", user, false); // 向用户发送一条临时消息（仅该用户可见）
            channel.sendMessage("我是晖哥的爹。",false) ;
            return 1; // 如果返回0则表示失败，返回其他大于0的数字表示成功
        }));
        // 变参指令，如用户输入“.khljava @用户”则会调用该方法
        // 其他参数类型还有ChannelArgumentType, RoleArgumentType, MessageArgumentType (Kaiheila.java提供)
        // StringArgumentType, IntegerArgumentType, LongArgumentType, DoubleArgumentType, FloatArgumentType, BoolArgumentType (Brigadier自带)
        command.then(Command.create("user" /* 此处为参数名，用以获取变量用，不可重复 */).argument(UserArgumentType.user(bot) /* 获取参数类型的对象 */).executes(context -> {
            CommandSender sender = context.getSource(); // 获取命令发送者
            Channel channel = sender.getChannel(); // 获取发送频道
            User user = sender.getUser(); //获取发送用户
            User mentionedUser = UserArgumentType.getUser(context, "user"); // 获取参数输入的变量
            channel.sendTempMessage("你调用了khljava @Someuser指令！并且输入了一名用户，其名为：" + mentionedUser.getUsername(), user, false); // 向用户发送一条临时消息（仅该用户可见）
            return 1; // 如果返回0则表示失败，返回其他大于0的数字表示成功
        }));
        // 注册指令，默认前缀为“.”
        // 在创建指令时：
        // Command.create("指令名称").addPrefix(".").addPrefix("/").literal(); 来使用别的前缀
        // Command.create("指令名称").addAlias("aa").addAlias("bb").literal(); 来添加别名
        // Command.create("指令名称").withRegex("正则"); 来使用Regex （此时别名和前缀就没有用处了)
        bot.getCommandManager().register(command);

        // 运行机器人
        if (bot.start()) {

            // 游戏相关API请求过程比较缓慢，请耐心等待
            // 创建游戏
//            Game kaiheilaJava = bot.createGame("Kaiheila.java");
//            // 设置正在游玩的游戏
//            bot.play(kaiheilaJava);
//
//            // 上传文件
//            File file = new File("self.jpg");
//            System.out.println(bot.uploadAsset(file));
//
//            // 运行异步任务，但是，现在用不了，不知道为什么，以后修
//            bot.getScheduler().runTaskAsynchronously(() -> {
//                // Do sth
//            });

            System.out.println("登录成功");
            try {
                TimeUnit.SECONDS.sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
                bot.shutdown();
            }
        } else {
            // 使用Logger输出运行失败
            System.out.println("登录失败");
        }
    }

    /**
     * 创建用户事件处理器
     */
    public static class UserEventHandler implements Listener {
     //   Logger logger = LoggerFactory.getLogger(UserEventHandler.class);
        /**
         * 接收消息更新事件
         *
         * @param event 更新文本消息事件内容
         */
        @EventHandler
        public void justRenameAsYouWant(UpdateMessageEvent event) {
          //  logger.info("[{}]{}",event.getEventAuthorId(), event.getEventContent());
            event.getChannel().sendMessage("You updated message", false);
        }
    }
}