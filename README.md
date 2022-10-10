# BC_UHC_Plugin_1.19.X
A UHC Plugin for Birdcraft
The plugin adds 4 commands to the game in order to configure and run a UHC event.

Configure WorldBorder Command
This command is used to display or edit the settings that will be used for the next launch of a UHC game.
Using the command without any other arguments will return a list of the current settings.
  Input: /worldborderconfigure or /wbconfigure
  Returns:  Current UHC Settings:
            UHC World Border Starting Size: 2046.0
            UHC World Border Minimum Size: 100.0
            UHC Shrink Duration (seconds): 3600
            UHC World Border Center: [-511.0, 0.0]
            Finale World Border Size: 50.0
            Finale World Border Center: [-381.0, -56.0]
            Finale Teleport Location: [-381.0, 68.0, -57.0]
            Player Spread Range: 1000.0
These are the default settings that will be used for a uhc on startup.
You can edit each setting with the same worldborderconfigure command.
  Input: /wbconfigure uhcmax <Number>
  Description: Used to edit the size that the worldborder will start at at the beginning of the uhc.
  Input: /wbconfigure uhcmin <Number>
  Description: Used to edit the size that the worldborder will shirnk to at the end of the timer.
  Input: /wbconfigure 
