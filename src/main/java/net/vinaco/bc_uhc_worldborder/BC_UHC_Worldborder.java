package net.vinaco.bc_uhc_worldborder;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.WorldBorder;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Arrays;


public final class BC_UHC_Worldborder extends JavaPlugin implements Listener {

    private double bordersize;
    private double uhcMax = 2046;
    private long uhcDuration = 3600;
    private double uhcMin = 100;
    private double[] uhcCenter = {-511, 0};
    private double finaleSize = 50;
    private double[] finaleCenter = {-381, -56};
    private double[] finaleLocation = {-381, 68, -57};
    private double spreaddistance = 200;
    private double spreadrange = 1000;
    private int timer;

    BossBar timerbar;
    BukkitTask uhcstart;
    BukkitTask end;
    WorldBorder border;
    private boolean isfinale = false;


    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getCommand("uhcconfigure").setTabCompleter(new TabComplete());
        timerbar = Bukkit.getServer().createBossBar("World Border Timer", BarColor.BLUE, BarStyle.SEGMENTED_20);
        timerbar.setVisible(false);
        getServer().dispatchCommand(getServer().getConsoleSender(), "worldborder set " + uhcMax);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        timerbar.addPlayer(player);
        //player.sendMessage("Hi");
    }

    @EventHandler
    public void onCompass(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getItem().getType() != Material.COMPASS) return;
        double distance = distancefromborder(p);
        getServer().dispatchCommand(getServer().getConsoleSender(), "title " + p.getName() + " actionbar \"Distance From Border: " + (int) distance + " Blocks\"");
    }


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.hasPermission("op")) {
                if (command.getName().equalsIgnoreCase("startuhc")) {
                    UHCStart(p);
                    end = new BukkitRunnable() {
                        @Override
                        public void run() {
                            UHCFinale();
                        }
                    }.runTaskLater(this, (uhcDuration + 2) * 20);
                }
                else if (command.getName().equalsIgnoreCase("uhcconfigure")) {
                    if (args.length >= 2) {

                        if (args[0].equalsIgnoreCase("uhcmax")) {
                            uhcMax = Double.parseDouble(args[1]);
                        } else if (args[0].equalsIgnoreCase("uhcmin")) {
                            uhcMin = Double.parseDouble(args[1]);
                        } else if (args[0].equalsIgnoreCase("uhcduration")) {
                            uhcDuration = Long.parseLong(args[1]);
                        } else if (args[0].equalsIgnoreCase("uhccenter")) {
                            if (args.length == 3) {
                                uhcCenter[0] = Double.parseDouble(args[1]);
                                uhcCenter[1] = Double.parseDouble(args[2]);
                            } else {
                                p.sendMessage(ChatColor.RED + "Error: Invalid number of arguments");
                            }
                        } else if (args[0].equalsIgnoreCase("finalesize")) {
                            finaleSize = Double.parseDouble(args[1]);
                        } else if (args[0].equalsIgnoreCase("finalecenter")) {
                            if (args.length == 3) {
                                finaleCenter[0] = Double.parseDouble(args[1]);
                                finaleCenter[1] = Double.parseDouble(args[2]);
                            } else {
                                p.sendMessage(ChatColor.RED + "Error: Invalid number of arguments");
                            }
                        } else if (args[0].equalsIgnoreCase("finalelocation")) {
                            if (args.length == 4) {
                                finaleLocation[0] = Double.parseDouble(args[1]);
                                finaleLocation[1] = Double.parseDouble(args[2]);
                                finaleLocation[2] = Double.parseDouble(args[3]);
                            } else {
                                p.sendMessage(ChatColor.RED + "Error: Invalid number of arguments");
                            }
                        } else if (args[0].equalsIgnoreCase("spreaddistance")) {
                            spreaddistance = Double.parseDouble(args[0]);
                        } else if (args[0].equalsIgnoreCase("spreadrange")) {
                            spreadrange = Double.parseDouble(args[0]);
                        }
                        p.sendMessage("Current UHC Settings:");
                        p.sendMessage("UHC World Border Starting Size: " + ChatColor.BLUE + uhcMax);
                        p.sendMessage("UHC World Border Minimum Size: " + ChatColor.BLUE + uhcMin);
                        p.sendMessage("UHC Shrink Duration (seconds): " + ChatColor.BLUE + uhcDuration);
                        p.sendMessage("UHC World Border Center: " + ChatColor.BLUE + Arrays.toString(uhcCenter));
                        p.sendMessage("Finale World Border Size: " + ChatColor.BLUE + finaleSize);
                        p.sendMessage("Finale World Border Center: " + ChatColor.BLUE + Arrays.toString(finaleCenter));
                        p.sendMessage("Finale Teleport Location: " + ChatColor.BLUE + Arrays.toString(finaleLocation));
                        p.sendMessage("Player Spread Range: " + ChatColor.BLUE + spreadrange);
                    } else if (args.length == 0) {
                        p.sendMessage("Current UHC Settings:");
                        p.sendMessage("UHC World Border Starting Size: " + ChatColor.BLUE + uhcMax);
                        p.sendMessage("UHC World Border Minimum Size: " + ChatColor.BLUE + uhcMin);
                        p.sendMessage("UHC Shrink Duration (seconds): " + ChatColor.BLUE + uhcDuration);
                        p.sendMessage("UHC World Border Center: " + ChatColor.BLUE + Arrays.toString(uhcCenter));
                        p.sendMessage("Finale World Border Size: " + ChatColor.BLUE + finaleSize);
                        p.sendMessage("Finale World Border Center: " + ChatColor.BLUE + Arrays.toString(finaleCenter));
                        p.sendMessage("Finale Teleport Location: " + ChatColor.BLUE + Arrays.toString(finaleLocation));
                        p.sendMessage("Player Spread Distance From Each Other: " + ChatColor.BLUE + spreaddistance);
                        p.sendMessage("Player Spread Range: " + ChatColor.BLUE + spreadrange);
                    } else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure\" to display the current World Border configuration");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure uhcmax <UHC World Border Starting Size>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure uhcmin <UHC World Border Minimum Size>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure uhcduration <UHC World Border Shrinking Duration>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure uhccenter <UHC World Border Center>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure finalesize <Finale World Border Size>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure finalecenter <Finale World Border Center>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure finalelocation <Finale Teleport Location>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure spreaddistance <Player Spread Distance From Each Other>\"");
                        p.sendMessage(ChatColor.GREEN + "\"/uhcconfigure spreadrange <Player Spread Range>\"");
                    } else {
                        p.sendMessage(ChatColor.RED + "Invalid number of arguments. Use \"/uhcconfigure help\" for help with the command.");
                    }
                }
                else if (command.getName().equalsIgnoreCase("finale")) {
                    if (!end.isCancelled()) {
                        end.cancel();
                    }
                    timerbar.setProgress(0);
                    UHCFinale();
                }
                else if (command.getName().equalsIgnoreCase("enduhc")){
                    UHCEnd();
                }
            } else {
                p.sendMessage("You don't have permission for that command!");
            }
        }


        return true;
    }

    public void UHCStart(Player p) {
        isfinale = false;
        String cmd = "spawnpoint @a " + (int) finaleLocation[0] + " " + (int) finaleLocation[1] + " " + (int) finaleLocation[2];
        getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
        cmd = "spreadplayers " + uhcCenter[0] + " " + uhcCenter[1] + " " + spreaddistance + " " + spreadrange + " true @a";
        getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
        getServer().dispatchCommand(getServer().getConsoleSender(), "clear @a");
        getServer().dispatchCommand(getServer().getConsoleSender(), "gamemode survival @a");
        getServer().dispatchCommand(getServer().getConsoleSender(), "give @a compass");
        getServer().dispatchCommand(getServer().getConsoleSender(), "effect give @a instant_health 255 100");
        getServer().dispatchCommand(getServer().getConsoleSender(), "effect give @a saturation 255 100");
        border = p.getLocation().getWorld().getWorldBorder();
        border.setSize(uhcMax);
        border.setSize(uhcMin, uhcDuration);
        border.setCenter(uhcCenter[0], uhcCenter[1]);

        timerbar.addPlayer(p);

        p.sendMessage("Set world border to " + uhcMax + " with min size of " + uhcMin + " and duration of " + uhcDuration + "!");
        timerbar.setVisible(true);
        bordersize = border.getSize();
        timer = (int) uhcDuration;

        uhcstart = new BukkitRunnable() {

            @Override
            public void run() {
                if (bordersize > uhcMin) {
                    bordersize = border.getSize();
                    timerbar.setProgress((bordersize - uhcMin) / (uhcMax - uhcMin));
                    timerbar.setTitle("§cTime Until Final Battle: §e" + timer);
                    timerbar.setVisible(true);
                    timer--;

                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    public void UHCEnd() {
        isfinale = false;
        if (!uhcstart.isCancelled()){
            uhcstart.cancel();
        }
        if (!end.isCancelled()){
            end.cancel();
        }
        String cmd = "tp @a " + finaleLocation[0] + " " + finaleLocation[1] + " " + finaleLocation[2];
        border.setSize(uhcMax);
        timerbar.setVisible(false);
    }

    public void UHCFinale() {
        isfinale = true;
        if (!uhcstart.isCancelled()) {
            uhcstart.cancel();
        }
        timerbar.setTitle("§cFinale");
        Bukkit.broadcastMessage("Teleporting you to the final battle in 5 seconds...");
        BukkitTask finaletp = new BukkitRunnable() {
            @Override
            public void run() {
                String cmd = "tp @a " + finaleLocation[0] + " " + finaleLocation[1] + " " + finaleLocation[2];
                getServer().dispatchCommand(getServer().getConsoleSender(), cmd);
                getServer().dispatchCommand(getServer().getConsoleSender(), "effect give @a instant_health 255 100");
                getServer().dispatchCommand(getServer().getConsoleSender(), "effect give @a saturation 255 100");
                border.setSize(finaleSize);
                border.setCenter(finaleCenter[0], finaleCenter[1]);
            }
        }.runTaskLater(this, (5) * 20);

    }

    public double distancefromborder(Player p) {

        double borderdistance;
        double xdistance;
        double zdistance;
        bordersize = border.getSize();
        if (isfinale) {
            xdistance = p.getLocation().getX() - finaleCenter[0];
            zdistance = p.getLocation().getZ() - finaleCenter[1];
        } else {
            xdistance = p.getLocation().getX() - uhcCenter[0];
            zdistance = p.getLocation().getZ() - uhcCenter[1];
        }
        if (Math.abs(xdistance) < Math.abs(zdistance)) {
            //closest to north or south border
            borderdistance = Math.abs((bordersize / 2) - zdistance);
        } else {
            //closest to east or west border
            borderdistance = Math.abs((bordersize / 2) - xdistance);
        }

        return borderdistance;
    }

}
