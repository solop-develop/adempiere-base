/** ***************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2014 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Raul Muñoz www.erpcya.com		                          *
 * ****************************************************************************/

package org.adempiere.pos;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.component.Borderlayout;
import org.adempiere.webui.component.Button;
import org.adempiere.webui.component.Grid;
import org.adempiere.webui.component.GridFactory;
import org.adempiere.webui.component.Label;
import org.adempiere.webui.component.Panel;
import org.adempiere.webui.component.Row;
import org.adempiere.webui.component.Rows;
import org.adempiere.webui.component.Window;
import org.adempiere.webui.window.FDialog;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zul.Timer;


/**
 * Component allows display the POS password Pin
 *  eEvolution author Victor Perez <victor.perez@e-evolution.com>, Created by e-Evolution on 26/01/16.
 */
public class WPOSUserPinListener implements EventListener {
    
	/**
	 * Constructor
	 * @param pos
	 */
	WPOSUserPinListener(WPOS pos)
    {
        this.pos = pos;
    }
	
    private WPOS 			pos;
    private Timer 			userPinTimer = null;
	/** Actions 						*/
	private Button 			b_ok 		 = new Button("Ok");
	private Button 			b_cancel	 = new Button("Cancel");
	/** Window	 						*/
	private Window 			w_alert;
	/** Field Password					*/
	private WPOSTextField	passwordField;
    private static boolean active = true;
    private boolean isKeyboard = false;
    
    public static void setActive(boolean active){
        WPOSUserPinListener.active = active;
    }

    public void setTimer(Timer timer)
    {
        this.userPinTimer = timer;
    }

    protected void doPerformAction(Event e)
    {
        if(e.getTarget()==userPinTimer) {
            pos.setIsCorrectUserPin(false);
            userPinTimer.stop();
            return;
        }
        
        if (userPinTimer.isRunning())
            return;

        if (!pos.isRequiredPIN())
            return;

        passwordField = new WPOSTextField("", pos.getKeyboard());

        passwordField.setStyle("Font-size:medium; font-weight:bold");
        passwordField.addEventListener(this);
        passwordField.setType("password");
        w_alert = new Window();
		Panel mainPanel = new Panel();
		w_alert.setWidth("200px");
		w_alert.setHeight("100px");
		
		Borderlayout mainLayout = new Borderlayout();
		Grid layout = GridFactory.newGridLayout();
		mainPanel.appendChild(mainLayout);
		mainPanel.setStyle("width: 100%; height: 100%; padding: 0; margin: 0");
		mainLayout.setHeight("100%");
		mainLayout.setWidth("100%");

		Panel centerPanel = new Panel();
		Center center = new Center();
		center.setStyle("border: none");
		mainLayout.appendChild(center);
		center.appendChild(centerPanel);
		centerPanel.appendChild(layout);
		layout.setWidth("100%");
		layout.setHeight("100%");
		w_alert.appendChild(mainPanel);
		Rows rows = null;
		Row row = null;
		rows = layout.newRows();
		row = rows.newRow();

        Label msg = new Label(Msg.parseTranslation(pos.getCtx() , "@UserPIN@"));
        row.appendChild(msg);
        
        row = rows.newRow();
		row.appendChild(passwordField);

		row.setSpans("2");
		row = rows.newRow();
		row.appendChild(b_ok);
		row.appendChild(b_cancel);

		b_ok.addActionListener(this);
		b_cancel.addEventListener("onClick", this);
        
		AEnv.showWindow(w_alert);
    }

	@Override
	public void onEvent(Event e) throws Exception {
		if(e.getTarget().equals(passwordField.getComponent(WPOSTextField.SECONDARY))
			&& e.getName().equals(Events.ON_FOCUS) && !isKeyboard){
				isKeyboard = true;
				passwordField.showKeyboard();
				passwordField.setFocus(true);
				return;
		}
		if(e.getTarget().equals(passwordField.getComponent(WPOSTextField.PRIMARY)) 
		   && e.getName().equals(Events.ON_FOCUS)){
			isKeyboard = false;
			return;
		}
		if(e.getTarget().equals(b_ok)){
			pos.setIsCorrectUserPin(passwordField.getText().toCharArray());
			w_alert.dispose();
		} else if(e.getTarget().equals(b_cancel)){
			pos.setIsCorrectUserPin(false);
			w_alert.dispose();
		}
		else if(active){
            try {
                doPerformAction(e);
            }
            catch (AdempiereException exception) {
                FDialog.error(pos.getWindowNo(), pos.getForm(), exception.getLocalizedMessage());
                throw new AdempiereException(exception.getMessage());
            }
        }		
	}
	
}
