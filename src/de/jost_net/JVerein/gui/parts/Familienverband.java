/**********************************************************************
 * $Source$
 * $Revision$
 * $Date$
 * $Author$
 *
 * Copyright (c) by Heiner Jostkleigrewe
 * This program is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,  but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See 
 *  the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, 
 * see <http://www.gnu.org/licenses/>.
 * 
 * heiner@jverein.de
 * www.jverein.de
 **********************************************************************/
package de.jost_net.JVerein.gui.parts;

import java.rmi.RemoteException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;

import de.jost_net.JVerein.JVereinPlugin;
import de.jost_net.JVerein.gui.control.MitgliedControl;
import de.jost_net.JVerein.keys.ArtBeitragsart;
import de.jost_net.JVerein.rmi.Beitragsgruppe;
import de.willuhn.jameica.gui.Part;
import de.willuhn.jameica.gui.util.Container;
import de.willuhn.jameica.gui.util.SimpleContainer;
import de.willuhn.jameica.gui.util.TabGroup;

public class Familienverband implements Part
{
  private TabFolder tab;

  private Beitragsgruppe gruppe;

  private MitgliedControl control;

  private Container cont;
  private Container contPlatzhalter;
  
  private boolean visible;
  
  private Composite parent;


  public Familienverband(MitgliedControl control, Beitragsgruppe gruppe)
  {
    this.control = control;
    this.gruppe = gruppe;
    this.visible = true;
    this.parent = null;
    this.cont = null;
    this.tab = null;
  }

  /**
   * Zeichnet den Familienverband Part. Die Ausgabe h�ngt von Feld visible
   * ab, das �ber die Funktion setVisible(boolean) gesetzt wird.
   */
  @Override
  public void paint(Composite parent) throws RemoteException
  {
    if(this.parent == null)
    {
      this.parent = parent;
    }
    
    //Familienverband soll nicht angezeigt werden...
    if(visible == false) 
    {
      if(contPlatzhalter == null)
      {
        contPlatzhalter = new SimpleContainer(parent);
        //Mache Platzhalter so klein wie m�glich:
        final GridData grid = new GridData(GridData.FILL_HORIZONTAL);
        grid.heightHint = 1;
        contPlatzhalter.getComposite().setLayoutData(grid);
        //Alternativ kann Textfeld angezeigt werden:
        //contPlatzhalter.addText(JVereinPlugin.getI18n().tr("Ohne Familienzugeh�rigkeit"), false);
      }
      return;
    }
    
    //Familienverband soll angezeigt werden...
    
    //Hier beginnt das eigentlich Zeichnen des Familienverbandes:
    if(cont == null)
      cont = new SimpleContainer(parent, true, 5);
    
    final GridData grid = new GridData(GridData.FILL_HORIZONTAL);
    grid.grabExcessHorizontalSpace = true;
    cont.getComposite().setLayoutData(grid);
    
    boolean zeichneFamilienverbandAlsTabGroup = true;
    if(zeichneFamilienverbandAlsTabGroup)
    {
      final GridData g = new GridData(GridData.FILL_HORIZONTAL);
      
      tab = new TabFolder(cont.getComposite(), SWT.NONE);
      tab.setLayoutData(g);
      TabGroup tg1 = new TabGroup(tab, JVereinPlugin.getI18n().tr(
          "Familienverband"));
      control.getFamilienangehoerigenTable().paint(tg1.getComposite());
      TabGroup tg2 = new TabGroup(tab, JVereinPlugin.getI18n().tr(
          "Zahlendes Familienmitglied"));
      //erstelle neuen zahler: (force == true)
      control.getZahler(true).setComment(
          JVereinPlugin.getI18n().tr(
              "Nur f�r Beitragsgruppenart: \"Familie: Angeh�rige\""));
      tg2.addLabelPair("Zahler", control.getZahler());
  
      if (gruppe != null)
      {
        setBeitragsgruppe(gruppe);
      }
    }
    else
    {
      tab = null;
    //erstelle neuen zahler: (force == true)
      cont.addLabelPair(JVereinPlugin.getI18n().tr("Zahler"), control.getZahler(true));
      control.getZahler().setMandatory(true);
      cont.addPart(control.getFamilienangehoerigenTable());
    }
    
  }

  /**
   * Aktiviert den ersten Tab, wenn Beitragsgruppe FAMILIE_ZAHLER ist,
   * ansonsten den zweiten Tab.
   * So kann f�r Mitglieder deren Beitragsgruppe FAMILIE_ANGEHOERIGER
   * direkt auf dem zweiten Tab ihr Familien-Zahler eingestellt werden.
   * @param gruppe
   */
  public void setBeitragsgruppe(Beitragsgruppe gruppe)
  {
    this.gruppe = gruppe;
    if(tab == null)
      return;
    try
    {
      if (gruppe.getBeitragsArt() == ArtBeitragsart.FAMILIE_ZAHLER)
        tab.setSelection(0);
      else if (gruppe.getBeitragsArt() == ArtBeitragsart.FAMILIE_ANGEHOERIGER)
        tab.setSelection(1);
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
    }
    tab.redraw();
    tab.layout(true);
  }

  /**
   * Zeige GUI-Komponente f�r Familienverband an oder blendet diese aus.
   * Ist showFamilienverband == false, wird ein Platzhalter angezeigt.
   * @param showFamilienverband
   */
  public void setVisible(boolean showFamilienverband)
  {
    if(this.visible == showFamilienverband)
      return;
    this.visible = showFamilienverband;

    if(showFamilienverband == false)
    {
      //l�sche cont, damit in paint() contPlatzhalter gemalt werden kann..
      if(cont != null)
      {
        cont.getComposite().dispose();
        cont = null;
      }
    }
    else if(showFamilienverband)
    {
      //l�sche Platzhalter contPlatzhalter, damit in paint() Familienverband gemalt werden kann.
      if(contPlatzhalter != null)
      {
        contPlatzhalter.getComposite().dispose();
        contPlatzhalter = null;
      }
    }
    
    if(parent != null)
    {
      try
      {
        paint(parent);
      }
      catch (RemoteException e)
      {
        e.printStackTrace();
      }
      updateGUI();
    }
  }
  
  private void updateGUI()
  {
    // Beim Ein- und Ausblenden der Familienverband-Tabelle muss 
    // die Gr��e des Rahmens neuberechnet werden 
    // und auch die Gr��e und Positionen der Geschwister des Rahmens! -->
    parent.getParent().getParent().getParent().pack(true);
  }

}
