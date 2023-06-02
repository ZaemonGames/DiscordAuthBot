package jp.zaemongames.discordauthbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

public class JoinListener extends ListenerAdapter {

    Guild guild = DiscordAuthBot.jda.getGuildById("1110891373374218320");

    Role memberRole = guild.getRoleById("1113047243776335893");

    TextChannel rulesChannel = guild.getRulesChannel();

    Category authCategory = guild.getCategoryById("1113081336022958091");

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);

        Member member = event.getMember();


        if (event.getGuild().equals(guild)) {
            for (TextChannel channel : authCategory.getTextChannels()) {
                if (channel.getName().equals(member.getId())) {
                    channel.delete().queue();
                }
            }

            TextChannel channel = authCategory.createTextChannel(event.getMember().getId()).complete();

            // @everyoneの権限を非表示にする
            channel.upsertPermissionOverride(guild.getPublicRole()).deny(Permission.VIEW_CHANNEL).complete();

            // 特定のユーザーに対して権限を設定する
            channel.upsertPermissionOverride(member).grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_HISTORY).complete();

            EmbedBuilder builder = new EmbedBuilder();
            builder.addField("ざえもん鯖へようこそ！", "参加するには" + rulesChannel.getAsMention() + "を読んでください！\nこれは簡易的な認証も兼ねています。", false);
            channel.sendMessage(member.getAsMention())
                    .addEmbeds(builder.build())
                    .queue();
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        super.onButtonInteraction(event);

        Member member = event.getMember();

        if (event.getButton().getId().equals("readRules")) {
            for (TextChannel channel : authCategory.getTextChannels()) {
                if (channel.getName().equals(member.getId())) {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.addField("ありがとうございます！", "元気よく挨拶をしてサーバーでプレイしましょう！\n※簡単で結構です", false);
                    channel.sendMessage(member.getAsMention())
                            .addEmbeds(builder.build())
                            .addActionRow(Button.primary("createintro", "参加する！"))
                            .queue();
                }
            }
        } else if (event.getButton().getId().equals("createintro")) {
            for (TextChannel channel : authCategory.getTextChannels()) {
                if (channel.getName().equals(member.getId())) {
                    channel.delete().queue();
                    guild.addRoleToMember(member, memberRole).queue();
                }
            }
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        super.onGuildMemberRemove(event);
        Member member = event.getMember();

        for (TextChannel channel : authCategory.getTextChannels()) {
            if (channel.getName().equals(member.getId())) {
                channel.delete().queue();
            }
        }
    }
}
