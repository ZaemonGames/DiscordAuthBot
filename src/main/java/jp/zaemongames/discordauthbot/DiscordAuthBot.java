package jp.zaemongames.discordauthbot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.bukkit.plugin.java.JavaPlugin;

public final class DiscordAuthBot extends JavaPlugin {

    static JDA jda;

    @Override
    public void onEnable() {

        JDABuilder builder = JDABuilder.createDefault("MTExMDkwNjc4NDgzMjU1MzAzMQ.GcRKNE.vDo9n-rQ1hKugZC8QSCxvSK7CBXDMbtpwsxBDc");
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.competing("Minecraft"));
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MESSAGES);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        jda = builder.build();
        try {
            jda.awaitReady();
            jda.addEventListener(new JoinListener());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDisable() {
        jda.shutdown();
    }
}
