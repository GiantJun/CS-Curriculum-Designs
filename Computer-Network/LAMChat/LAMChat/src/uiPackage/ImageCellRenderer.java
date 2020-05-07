package uiPackage;
/** * @author  ���� :GiantJun 
* @date ����ʱ�䣺2019��6��29�� ����11:50:45 
* @version 1.0 
* <p>Description: </p>   
* @throws 
*/

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import dataStructure.OnlineMap;
import dataStructure.User;

class ImageCellRenderer extends JPanel implements ListCellRenderer{
    private ImageIcon icon ;
    private String name;
    //������Ƶ�Ԫ��ʱ����ɫ
    private Color background;
    //ǰ��ɫ
    private Color foreground;
    
    private OnlineMap<String,User> map;
    
    public ImageCellRenderer(OnlineMap<String,User> userMap) {
    	this.map = userMap;
    }
 
    public Component getListCellRendererComponent(JList list,
        Object value,int index,boolean isSelected,boolean cellHasFocus){
        icon = new ImageIcon("user2.png");
        name = value.toString();
        background = isSelected ? list.getSelectionBackground() : list.getBackground();
        foreground = isSelected ? list.getSelectionForeground() : list.getForeground();
        //���ظ�JPanel������Ϊ�б��������
        return this;
    }
    //��дpaintComponent()�������ı�JPanel�����
    public void paintComponent(Graphics g){
        int imageWidth = icon.getImage().getWidth(null);
        int imageHeight = icon.getImage().getHeight(null);
        g.setColor(background);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(foreground);
        //���ƺ���ͼ��
        g.drawImage(icon.getImage(),5,10,null);
        g.setFont(new Font("SansSerif",Font.BOLD,18));
        //���ƺ����û���
        g.drawString(name + "�����ߡ�",imageWidth+10,5+imageHeight/2);
        g.setFont(new Font("SansSerif",Font.BOLD,14));
        g.drawString("ip:"+map.get(name).ip,imageWidth+10,25+imageHeight/2);
    }
    //ͨ���÷��������ø�ImageCellRendderer����Ѵ�С
    public Dimension getPreferredSize(){
        return new Dimension(60,80);
    }
}
