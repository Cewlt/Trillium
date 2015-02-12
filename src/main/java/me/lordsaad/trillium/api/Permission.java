package me.lordsaad.trillium.api;

public class Permission {
    public static class Punish {
        private static final String PREFIX = "tr.punish.";
        public static final String MUTE = PREFIX + "mute";
        public static final String KICK = PREFIX + "kick";
        public static final String BAN = PREFIX + "ban";
        public static final String UNBAN = PREFIX + "unban";
    }

    public static class Ability {
        private static final String PREFIX = "tr.ability.";
        public static final String BACK = PREFIX + "back";
        public static final String FLY = PREFIX + "fly";
        public static final String FLY_OTHER = PREFIX + "fly.other";
        public static final String GOD = PREFIX + "god";
        public static final String GOD_OTHER = PREFIX + "god.other";
        public static final String VANISH = PREFIX + "vanish";
        public static final String VANISH_OTHER = PREFIX + "vanish.other";
        public static final String AFK = PREFIX + "afk";
    }

    public static class Admin {
        private static final String PREFIX = "tr.admin.";
        public static final String BROADCAST = PREFIX + "broadcast";
    }
}