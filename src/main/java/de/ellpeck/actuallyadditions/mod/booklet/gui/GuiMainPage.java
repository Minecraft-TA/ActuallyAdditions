///*
// * This file ("GuiMainPage.java") is part of the Actually Additions mod for Minecraft.
// * It is created and owned by Ellpeck and distributed
// * under the Actually Additions License to be found at
// * http://ellpeck.de/actaddlicense
// * View the source code at https://github.com/Ellpeck/ActuallyAdditions
// *
// * Â© 2015-2017 Ellpeck
// */
//
//package de.ellpeck.actuallyadditions.mod.booklet.gui;
//
//import com.mojang.blaze3d.matrix.MatrixStack;
//import de.ellpeck.actuallyadditions.api.ActuallyAdditionsAPI;
//import de.ellpeck.actuallyadditions.api.booklet.IBookletEntry;
//import de.ellpeck.actuallyadditions.mod.ActuallyAdditions;
//import de.ellpeck.actuallyadditions.mod.booklet.InitBooklet;
//import de.ellpeck.actuallyadditions.mod.booklet.button.EntryButton;
//import de.ellpeck.actuallyadditions.mod.data.PlayerData;
//import de.ellpeck.actuallyadditions.mod.data.PlayerData.PlayerSave;
//import de.ellpeck.actuallyadditions.mod.inventory.gui.TexturedButton;
//import de.ellpeck.actuallyadditions.mod.network.PacketHandlerHelper;
//import de.ellpeck.actuallyadditions.mod.util.StringUtil;
//import de.ellpeck.actuallyadditions.mod.util.Util;
//import net.minecraft.client.gui.screen.Screen;
//import net.minecraft.client.gui.widget.button.Button;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.util.text.TextFormatting;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
////TODO Fix achievement button
//@OnlyIn(Dist.CLIENT)
//public class GuiMainPage extends GuiBooklet {
//
//    private static final String[] QUOTES = new String[]{"Actually Additions, to me, is quite magical in a way.@Saphrym", "Actually quite cool. Lots of nice little additions.@Direwolf20", "Mod Dev quite rude and arrogant@Bubb1e0seven", "A whimsical breath of fresh air in a stuffy tech-mod world.@mezz", "User-friendly :3@TheMeeep", "A lot of stuff, some of it really good.@Narubion", "I like the bookmarks.@Vazkii", "It's got some stuff I guess.@Ellpeck", "Actually Additions should be included in every new modpack that includes any form of tech.@YasminEndusa", "A mod that basically lets you do what ever the heck you want.@Ristelle", "TINY TORCHES!! BABY TORCHES!! Somebody actually finally did it!!@Soaryn", "Balanced mod wich makes things different - in a good way.@garantiertnicht", "The mod everyone needs, but not everyone knows@Brewpl", "The in-game documentation is the best I’ve seen. I especially love the JEI integration. Even a derp like me can figure it out.@dannydjdk", "The second best mod I've ever used.@mmaas44", "The Fermenting Barrel is one of my favorite textures.@amadornes", "Smiley Clouds is the reason for fascism in 2016.@raoulvdberge", "The worms are an awesome idea!@greenking", "Can I use that mod in my pack?@Ibraheem", "Hello, love the mod.@SuntannedDuck2", "Quick! Have all the fun before they nerf it!@JuddMan03", "I have a feeling Actually Additions is also like Extra Utilities with Random things smashed together why is it...@lesslighter", "Leaf eater... munchdew... hummm@EiOs", "There is no such thing as canola seeds.@AlBoVa", "This mod is cancer, BRUTAL EXPENSIVE POWER usage..Just, cancer.@KoJo", "Spaghetti is spaghetti, and noodles are noodles.@robsonld04", "The Actually Additions name is actually true. It's actually great!@asiekierka", "Such a great mod@jsdeveloper", "That mod is kind of funny.@Anonymous", "Actually Additions is a lot of fun.@Anonymous", "Is Actually Additions still fugly?@Anonymous", "I like it, but it's so small.@Anonymous", "It has a couple of blocks I like, but overall it's just a mess.@Anonymous", "Direwolf's 1.10 playthrough is just him shilling Actually Additions@Anonymous", "We thought about sending the author a bunch of pizzas to his adress@Anonymous", "It's op as heck.@billofbong0", "Actually AdditionsってマイクラMODすごく良いのに日本人で遊んでる人あんまいないっぽい@stay_uk", "Actually Additions is OP. Not like my favorite combination of mods, Project E + Magic Crops + Draconic Evolution.@Anonymous", "To be perfectly honest, I never actually realized how much content Actually Additions has before.@Ellpeck", "I don't blame you, I actually downgraded to Actually Additions.@PvtSeaCow", "It is lonely because there is no device to fly items with the laser in the 1.7.10 version.@Google Translate", "始めまして。日本人です。このMODは本当に素晴らしい！ただ、1.7.10ヴァージョンだと、レーザーでアイテムを飛ばす装置がないので寂しいです。@Anonymous", "Some verses found in older translations, such as the KJV were actually additions made by later copyists.@Pat_Joel", "I can't place filters into Laser Relays, but the mod is very cool.@LP_Jakob", "Am I good enough to be an Actually Additions tool?@deanwhufc"};
//
//    //private TexturedButton achievementButton;
//    private TexturedButton configButton;
//
//    private Button tutorialButton;
//    private boolean showTutorial;
//
//    private String bookletName;
//    private String bookletEdition;
//
//    private List<String> quote;
//    private String quoteGuy;
//
//    public GuiMainPage(Screen previousScreen) {
//        super(previousScreen, null);
//    }
//
//    private static List<IBookletEntry> getDisplayedEntries() {
//        List<IBookletEntry> displayed = new ArrayList<>();
//
//        for (IBookletEntry entry : ActuallyAdditionsAPI.BOOKLET_ENTRIES) {
//            if (entry.visibleOnFrontPage()) {
//                displayed.add(entry);
//            }
//        }
//
//        return displayed;
//    }
//
//    @Override
//    public void init() {
//        super.init();
//
//        int flavor = 1;
//        if (this.getMinecraft().level.random.nextFloat() <= 0.1) {
//            flavor = MathHelper.nextInt(this.getMinecraft().level.random, 2, 7);
//        }
//        this.bookletName = "info." + ActuallyAdditions.MODID + ".booklet.manualName.1." + flavor;
//
//        String usedQuote = QUOTES[this.getMinecraft().level.random.nextInt(QUOTES.length)];
//        String[] quoteSplit = usedQuote.split("@");
//        if (quoteSplit.length == 2) {
//            //this.quote = this.font.listFormattedStringToWidth(quoteSplit[0], 120); //TODO wut
//            this.quoteGuy = quoteSplit[1];
//        }
//
//        String playerName = this.getMinecraft().player.getName().getString();
//        if (playerName.equalsIgnoreCase("dqmhose")) {
//            this.bookletEdition = "Pants Edition";
//        } else if (playerName.equalsIgnoreCase("TwoOfEight") || playerName.equalsIgnoreCase("BootyToast")) {
//            this.bookletEdition = "Illustrator's Edition";
//        } else if (playerName.equalsIgnoreCase("KittyVanCat")) {
//            this.bookletEdition = "Cat's Edition";
//        } else if (playerName.equalsIgnoreCase("canitzp")) {
//            this.bookletEdition = "P's Edition";
//        } else if (playerName.equalsIgnoreCase("direwolf20")) {
//            this.bookletEdition = "Edition 20";
//        } else if (playerName.equalsIgnoreCase("dannydjdk") || playerName.equalsIgnoreCase("andrew_period")) {
//            this.bookletEdition = "Derp's Edition";
//        } else if (playerName.equalsIgnoreCase("mezz")) {
//            this.bookletEdition = "Just Enough Editions";
//        } else if (playerName.equalsIgnoreCase("amadornes")) {
//            this.bookletEdition = "Beard's Edition";
//        } else if (playerName.equalsIgnoreCase("raoul")) {
//            this.bookletEdition = "Giraffe's Edition";
//        } else if (playerName.equalsIgnoreCase("ellpeck") || playerName.equalsIgnoreCase("profprospector")) {
//            String[] colors = new String[15];
//            for (int i = 0; i < colors.length; i++) {
//                colors[i] = TextFormatting.getById(this.getMinecraft().level.random.nextInt(15)).toString() + TextFormatting.ITALIC;
//            }
//            this.bookletEdition = String.format("%sC%so%sl%so%sr%sf%su%sl %sE%sd%si%st%si%so%sn", (Object[]) colors);
//        } else if (playerName.equalsIgnoreCase("oitsjustjose")) {
//            this.bookletEdition = "oitsjustanedition";
//        } else if (playerName.equalsIgnoreCase("xbony2")) {
//            this.bookletEdition = "Naughty Edition";
//        } else if (playerName.equalsIgnoreCase("themattabase")) {
//            this.bookletEdition = "Withered Edition";
//        } else if (playerName.equalsIgnoreCase("robsonld04")) {
//            this.bookletEdition = "Modpack Edition";
//        } else if (playerName.equalsIgnoreCase("snowshock35")) {
//            this.bookletEdition = "Edition 35";
//        } else if (playerName.equalsIgnoreCase("asiekierka")) {
//            this.bookletEdition = "â€½ Edition";
//        } else if (playerName.equalsIgnoreCase("elucent")) {
//            this.bookletEdition = "";
//        } else {
//            if (Util.isDevVersion()) {
//                this.bookletEdition = "Dev's Edition";
//            } else {
//                this.bookletEdition = StringUtil.localize("info." + ActuallyAdditions.MODID + ".booklet.edition") + " " + Util.getMajorModVersion();
//            }
//        }
//
//        List<String> configText = new ArrayList<>();
//        configText.add(TextFormatting.GOLD + StringUtil.localize("booklet." + ActuallyAdditions.MODID + ".configButton.name"));
//        //configText.addAll(this.font.listFormattedStringToWidth(StringUtil.localizeFormatted("booklet." + ActuallyAdditions.MODID + ".configButton.desc", ActuallyAdditions.NAME).replaceAll("\\\\n", "\n"), 200)); //TODO wut
//        this.configButton = new TexturedButton(RES_LOC_GADGETS, this.guiLeft + 16, this.guiTop + this.ySize - 30, 188, 14, 16, 16, configText, btn -> {
//        });
//        this.addButton(this.configButton);
//
//        List<String> achievementText = new ArrayList<>();
//        achievementText.add(TextFormatting.GOLD + StringUtil.localize("booklet." + ActuallyAdditions.MODID + ".achievementButton.name"));
//        //achievementText.addAll(this.font.listFormattedStringToWidth(StringUtil.localizeFormatted("booklet." + ActuallyAdditions.MODID + ".achievementButton.desc", ActuallyAdditions.NAME), 200)); //TODO wut
//        //this.achievementButton = new TexturedButton(RES_LOC_GADGETS, -389, this.guiLeft+36, this.guiTop+this.ySize-30, 204, 14, 16, 16, achievementText);
//        //this.addButton(this.achievementButton);
//
//        PlayerSave data = PlayerData.getDataFromPlayer(this.getMinecraft().player);
//        if (!data.didBookTutorial) {
//            this.showTutorial = true;
//
//            //this.tutorialButton = new GuiButton(666666, this.guiLeft + 140 / 2 - 50, this.guiTop + 146, 100, 20, "Please click me <3");
//            this.addButton(this.tutorialButton);
//
//            this.configButton.visible = false;
//            //this.achievementButton.visible = false;
//        }
//
//        for (int i = 0; i < BUTTONS_PER_PAGE; i++) {
//            List<IBookletEntry> displayed = getDisplayedEntries();
//            if (displayed.size() > i) {
//                IBookletEntry entry = displayed.get(i);
//                this.addButton(new EntryButton(this, i, this.guiLeft + 156, this.guiTop + 11 + i * 13, 115, 10, "- " + entry.getLocalizedNameWithFormatting(), ItemStack.EMPTY));
//            } else {
//                return;
//            }
//        }
//    }
///*
//    @Override
//    protected void actionPerformed(GuiButton button) throws IOException {
//        if (button instanceof EntryButton) {
//            List<IBookletEntry> displayed = getDisplayedEntries();
//            if (displayed.size() > button.id) {
//                IBookletEntry entry = displayed.get(button.id);
//                if (entry != null) {
//                    this.getMinecraft().setScreen(new GuiEntry(this.previousScreen, this, entry, 0, "", false));
//                }
//            }
//        }
//        /*else if(button == this.achievementButton){
//           GuiScreen achievements = new GuiAAAchievements(this, this.getMinecraft().player.getStatFileWriter());
//            this.getMinecraft().displayGuiScreen(achievements);
//        }*/
//    /*
//        else if (button == this.configButton) {
//            GuiScreen config = new GuiConfiguration(this);
//            this.getMinecraft().setScreen(config);
//        } else if (this.showTutorial && button == this.tutorialButton) {
//            if (this.hasBookmarkButtons()) {
//                if (!isShiftKeyDown()) {
//                    for (int i = 0; i < InitBooklet.chaptersIntroduction.length; i++) {
//                        this.bookmarkButtons[i].assignedPage = InitBooklet.chaptersIntroduction[i].getAllPages()[0];
//                    }
//                }
//                this.showTutorial = false;
//                this.tutorialButton.visible = false;
//
//                this.configButton.visible = true;
//                //this.achievementButton.visible = true;
//
//                PlayerSave data = PlayerData.getDataFromPlayer(this.getMinecraft().player);
//                data.didBookTutorial = true;
//                PacketHandlerHelper.sendPlayerDataToServer(false, 1);
//            }
//        } else {
//            super.actionPerformed(button);
//        }
//    }
//    */
//    @Override
//    public void drawScreenPre(MatrixStack stack, int mouseX, int mouseY, float partialTicks) {
//        super.drawScreenPre(stack, mouseX, mouseY, partialTicks);
//
//        String strg = TextFormatting.DARK_GREEN + StringUtil.localize(this.bookletName);
//        this.font.draw(stack, strg, this.guiLeft + 72 - this.font.width(strg) / 2 - 3, this.guiTop + 19, 0);
//        strg = TextFormatting.DARK_GREEN + StringUtil.localize("info." + ActuallyAdditions.MODID + ".booklet.manualName.2");
//        this.font.draw(stack, strg, this.guiLeft + 72 - this.font.width(strg) / 2 - 3, this.guiTop + 19 + this.font.lineHeight, 0);
//
//        strg = TextFormatting.GOLD + TextFormatting.ITALIC.toString() + this.bookletEdition;
//        this.font.draw(stack, strg, this.guiLeft + 72 - this.font.width(strg) / 2 - 3, this.guiTop + 40, 0);
//
//        if (this.showTutorial) {
//            String text = TextFormatting.BLUE + "It looks like this is the first time you are using this manual. \nIf you click the button below, some useful bookmarks will be stored at the bottom of the GUI. You should definitely check them out to get started with " + ActuallyAdditions.NAME + "! \nIf you don't want this, shift-click the button.";
//            this.renderSplitScaledAsciiString(text, this.guiLeft + 11, this.guiTop + 55, 0, false, this.getMediumFontSize(), 120);
//        } else if (this.quote != null && !this.quote.isEmpty() && this.quoteGuy != null) {
//            int quoteSize = this.quote.size();
//
//            for (int i = 0; i < quoteSize; i++) {
//                this.renderScaledAsciiString(TextFormatting.ITALIC + this.quote.get(i), this.guiLeft + 25, this.guiTop + 90 + i * 8, 0, false, this.getMediumFontSize());
//            }
//            this.renderScaledAsciiString("- " + this.quoteGuy, this.guiLeft + 60, this.guiTop + 93 + quoteSize * 8, 0, false, this.getLargeFontSize());
//        }
//    }
//
//    @Override
//    public void addOrModifyItemRenderer(ItemStack renderedStack, int x, int y, float scale, boolean shouldTryTransfer) {
//
//    }
//}
