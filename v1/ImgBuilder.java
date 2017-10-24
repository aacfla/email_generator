import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImgBuilder {

    public static void main(String[] args) {

        ArrayList<String> info = new ArrayList<String>();
        String title = "";
        String subtitle = "";
        info.add("");
        info.add("");
        info.add("");
        info.add("");
//        info.add("");

        int week = 4;
        int number = 3;
        String description = "";
        ImgBuilder builder = new ImgBuilder(title, subtitle, info, description);
        File file = builder.build(number);

        String accessKey = "accessKey";
        String secretKey = "secretKey";
        BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AWSStaticCredentialsProvider provider = new AWSStaticCredentialsProvider(credentials);
        AmazonS3ClientBuilder s3builder= AmazonS3ClientBuilder.standard().withCredentials(provider).withRegion("us-west-2");
        AmazonS3 s3 = s3builder.build();

        PutObjectRequest request = new PutObjectRequest("emailgenbucket", "Week " + week + "/img_" + number + title, file)
                .withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(request);

    }

    private String title;
    private String subtitle;
    private List<String> info;
    private String description;

    private static int WIDTH = 390;
    private static int HEIGHT = 250;

    int color_R[] = {180, 62, 181, 137, 100, 177, 102};
    int color_G[] = {203, 109, 221, 171, 181, 191, 204};
    int color_B[] = {232, 171, 249, 198, 246, 202, 255};

    public ImgBuilder(String title, String subtitle, List<String> info, String description) {
        this.title = title;
        this.subtitle = subtitle;
        this.info = info;
        this.description = description;
    }

    public File build(int color) {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        Font font = new Font("Arial", Font.PLAIN, 14);
        g2d.setFont(font);
        g2d.setFont(font);

        g2d.setColor(new Color(color_R[color], color_G[color], color_B[color]));

        g2d.fillRect(0,0, WIDTH, HEIGHT);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(239, 20, 3, 210);

        g2d = renderDescription(description, g2d);
        g2d = renderLeft(title,subtitle,info, 24, 18, 14, g2d);

        g2d.dispose();

        String filename = title + ".png";
        File file = new File(filename);
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return file;
    }

    private Graphics2D renderLeft(String title, String subtitle, List<String> info,
                                         int titleSize, int subtitleSize, int infoSize, Graphics2D g2d) {
        Font titleFont = new Font("Arial", Font.BOLD, titleSize);
        Font subtitleFont = new Font("Arial", Font.ITALIC, subtitleSize);
//        Font subtitleFont = titleFont;
        Font infoFont = new Font("Arial", Font.PLAIN, infoSize);

        FontMetrics titleFM = g2d.getFontMetrics(titleFont);
        FontMetrics subtitleFM = g2d.getFontMetrics(subtitleFont);
        FontMetrics infoFM = g2d.getFontMetrics(infoFont);

        int LEFT_HEIGHT = titleFM.getHeight()
                + subtitleFM.getHeight()
                + 3 * infoFM.getHeight()
                + 10;

        int leftStart = (HEIGHT - LEFT_HEIGHT)/2 + LEFT_HEIGHT + infoFM.getAscent();
        int curYPos = leftStart;

        for (int k = info.size() - 1; k >= 0; k--) {
            int leftOffset = (209 - infoFM.stringWidth(info.get(k)))/2;
            g2d.drawString(info.get(k), 15 + leftOffset, leftStart - (info.size() - k) * infoFM.getHeight());
            curYPos = leftStart - (info.size() - k) * infoFM.getHeight();
        }

        curYPos -= 5 + infoFM.getHeight();

        g2d.setFont(subtitleFont);
        int leftOffset = (209 - subtitleFM.stringWidth(subtitle))/2;
        g2d.drawString(subtitle, 15 + leftOffset, curYPos);
        curYPos -= 5 + subtitleFM.getHeight();

        g2d.setFont(titleFont);
        leftOffset = (209 - titleFM.stringWidth(title))/2;
        g2d.drawString(title, 15 + leftOffset, curYPos);

        return g2d;
    }

    private Graphics2D renderDescription(String message, Graphics2D g2d) {
        Font font = new Font("Arial", Font.PLAIN, 13);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        ArrayList<String> lines = splitMessage(message, fm);

        int descriptionHeight = lines.size() * fm.getHeight();
        int centered = (HEIGHT - descriptionHeight)/2 - fm.getDescent();

        for (int k = lines.size() - 1; k >= 0; k--) {
            g2d.drawString(lines.get(k), 256, centered + (k + 1) * fm.getHeight());
        }

        return g2d;
    }

    private ArrayList<String> splitMessage(String str,FontMetrics fm) {
        String[] parts = str.split(" ");
        ArrayList<String> pieces = new ArrayList<String>();
        for (int k = 0; k < parts.length; k++) {
            pieces.add(parts[k]);
        }

        ArrayList<String> lines = new ArrayList<String>();
        String previous = pieces.get(0);
        String current = previous;
        boolean flag = false;
        for (int k = 1; k < pieces.size(); k ++) {
            current = current + " " + pieces.get(k);

            if (fm.stringWidth(current) > 119) {
                lines.add(previous);
                previous = pieces.get(k);
                current = previous;
            } else {
                previous = current;
            }
        }

        lines.add(previous);

        return lines;
    }

}
