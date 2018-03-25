package de.kiyan.menuapi.Commands;

import de.kiyan.menuapi.Main;
import de.kiyan.menuapi.MenuAPI.InventoryClickType;
import de.kiyan.menuapi.MenuAPI.Menu;
import de.kiyan.menuapi.MenuAPI.MenuAPI;
import de.kiyan.menuapi.MenuAPI.MenuItem;
import de.kiyan.menuapi.Utils.SignInputAPI;
import de.kiyan.menuapi.Utils.SignInputEvent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class CMDMenu implements CommandExecutor, Listener
{
    @Override
    public boolean onCommand( CommandSender sender, Command cmd, String label, String[] args )
    {
        // Excluding non-players.
        if( !( sender instanceof Player ) )
        {
            sender.sendMessage( "§cYou must be a player" );

            return false;
        }

        // Being sure no other commands is being used.
        if( !label.equalsIgnoreCase( "test" ) )
        {
            return false;
        }

        Player player = ( Player ) sender;
        Menu menu = new MenuAPI().createMenu( "Test", 3 );

        menu.addMenuItem( new MenuItem( "test", new ItemStack( Material.ANVIL ) ) {
            @Override
            public void onClick( Player player, InventoryClickType clickType )
            {
                if( clickType.isLeftClick() )
                {
                    new SignInputAPI( ).openEditor( player );
                }
            }
        }, 0 );

        menu.openMenu( player );


        return false;
    }

    /*
     * This custom listener gets triggered, when the user closed the SignInputField.
     * It delievers information like  player, single signLines, Array signlines or getHandlers (???).
     *
     * Can also be placed on main class.
     */
    @EventHandler
    public void SignInput( SignInputEvent event )
    {
        Player player = ( Player ) event.getPlayer( );
        String signLines = event.getLine( 0 ) + event.getLine( 1 ) + event.getLine( 2 ) + event.getLine( 3 );

        if( signLines.equalsIgnoreCase( "" ) )
        {
            player.sendMessage( "You cannot leave it empty, please type again!" );
            new SignInputAPI( ).openEditor( player );

            return;
        }

        player.sendMessage( "Succesful! You just typed | §c" + signLines );
    }
}