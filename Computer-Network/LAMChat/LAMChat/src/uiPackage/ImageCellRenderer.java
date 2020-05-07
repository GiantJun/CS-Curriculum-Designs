package uiPackage;
/** * @author  作者 :GiantJun 
* @date 创建时间：2019年6月29日 下午11:50:45 
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
    //定义绘制单元格时背景色
    private Color background;
    //前景色
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
        //返回该JPanel对象作为列表项绘制器
        return this;
    }
    //重写paintComponent()方法，改变JPanel的外观
    public void paintComponent(Graphics g){
        int imageWidth = icon.getImage().getWidth(null);
        int imageHeight = icon.getImage().getHeight(null);
        g.setColor(background);
        g.fillRect(0,0,getWidth(),getHeight());
        g.setColor(foreground);
        //绘制好友图标
        g.drawImage(icon.getImage(),5,10,null);
        g.setFont(new Font("SansSerif",Font.BOLD,18));
        //绘制好友用户名
        g.drawString(name + "【在线】",imageWidth+10,5+imageHeight/2);
        g.setFont(new Font("SansSerif",Font.BOLD,14));
        g.drawString("ip:"+map.get(name).ip,imageWidth+10,25+imageHeight/2);
    }
    //通过该方法来设置该ImageCellRendderer的最佳大小
    public Dimension getPreferredSize(){
        return new Dimension(60,80);
    }
}
