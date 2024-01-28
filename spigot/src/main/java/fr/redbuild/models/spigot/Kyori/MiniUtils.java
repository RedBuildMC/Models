package fr.redbuild.models.spigot.Kyori;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;

public class MiniUtils {
    public static MiniMessage getMiniMessage(){
        return MiniMessage.builder()
        .tags(TagResolver.builder()
      .resolver(StandardTags.color())
      .resolver(StandardTags.decorations())
      .build()
    )
    .build();
    }
}
