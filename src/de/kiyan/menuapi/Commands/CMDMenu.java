package de.kiyan.menuapi.Commands;

import de.kiyan.menuapi.Main;
import de.kiyan.menuapi.Utils.*;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

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

        // Being sure no other commands are being used.
        if( !label.equalsIgnoreCase( "test" ) )
        {
            return false;
        }

        Player player = ( Player ) sender;

        /*
         * YesNoGUI is the confirmation menu. This can be used to double check the seriousness about the users choice.
         * @param title - Define your custom chest name
         * @param plugin - return your main instance to get it working.
         *
         * onFinish. Represent the user choice.
         *           Response.YES == When he clicked on 'Yes' and initiate the code below.
         *           and
         *           REsponse.NO == When he clicked on 'No'
         *
         * Optional: You can also override onClose( ) & onClick( ) to change behavior ( but not needed ).
         *
         */
        YesNoGUI menu2 = new YesNoGUI( "§2§lAre you sure?", Main.getInstance )
        {
            @Override
            public void onFinish( Response response )
            {
                if( response == Response.YES )
                {
                    // Initiate your sign prompt.
                    SignInputAPI Sign = new SignInputAPI( );

                    Sign.openEditor( player );
                }

                if( response == Response.NO )
                {
                    player.sendMessage( "You clicked no!" );
                }
            }
        };

        /*
         * MenuGUI is the Core function.
         *
         * @params title - Define your custom chest name
         * @param size - Define your rows ( notice only 9er steps.. 9, 18, 27 )
         * @param plugin - return your main instance to get it working.
         *
         * You can add as much items as you like by simple adding an .addOption to end line
         * .addOption( new ItemStack( Material.DIAMOND ), 2 ).addOption( new ItemStack( Material.GOLD_INGOT ), 3 );
         * or using ItemBuilder makes it easier to customize items.
         *
         * .addOption( new ItemBuilder( Material.DIAMOND ).setName( "§cHerbert" ).setLore( "Fuck", "mah", "Butt" ).toItemStack( ), 2 );
         */
        MenuGUI menu = new MenuGUI( "§cMy Menu", 18, Main.getInstance )
        {
            @Override
            public void onClose( InventoryCloseEvent e ) { }

            // Handling of inventory items
            @Override
            public void onClick( InventoryClickEvent e )
            {
                Player p = ( Player ) e.getWhoClicked( );

                ItemStack is = e.getCurrentItem( );

                if( is == null )
                    return;

                // Configurate click type ClickType.LEFT an so on..
                if( e.getClick() == ClickType.LEFT && is.getType( ) == Material.DIAMOND )
                {
                    menu2.show( player );
                }

                if( e.getClick() == ClickType.RIGHT && is.getType( ) == Material.DIAMOND )
                {
                    player.sendMessage( "This" );
                    player.closeInventory(); // to close current menu
                }
            }
        }.addOption( new ItemBuilder( Material.DIAMOND ).setName( "§cHerbert" ).toItemStack( ), 2 ); // You can add as much as you have space for items.

        menu.show( player );

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
