package main.java.Models;

public class BuildImageRequest {
    private String quarter;
    private int week;
    private int num;
    private String title;
    private String subtitle;
    private String info1;
    private String info2;
    private String info3;
    private String info4;
    private String description;
    private String link;
    private int titleSize;
    private int subtitleSize;
    private int infoSize;
    private int descSize;

    public void setQuarter(String quarter) { this.quarter = quarter; }
    public String getQuarter() { return quarter; }

    public void setWeek(int week) { this.week = week; }
    public int getWeek() { return week; }

    public void setNum(int num) { this.num = num; }
    public int getNum() { return num; }

    public void setTitle(String title) { this.title = title; }
    public String getTitle() { return title; }

    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public String getSubtitle() { return subtitle; }

    public void setInfo1(String info1) { this.info1 = info1; }
    public String getInfo1() { return info1; }

    public void setInfo2(String info2) { this.info2 = info2; }
    public String getInfo2() { return info2; }

    public void setInfo3(String info3) { this.info3 = info3; }
    public String getInfo3() { return info3; }

    public void setInfo4(String info4) { this.info4 = info4; }
    public String getInfo4() { return info4; }

    public void setDescription(String description) { this.description = description; }
    public String getDescription() { return description; }

    public void setLink(String link) { this.link = link; }
    public String getLink() { return link; }

    public void setTitleSize(int titleSize) { this.titleSize = titleSize; }
    public int getTitleSize() { return titleSize; }

    public void setSubtitleSize(int subtitleSize) { this.subtitleSize = subtitleSize; }
    public int getSubtitleSize() { return subtitleSize; }

    public void setInfoSize(int infoSize) { this.infoSize = infoSize; }
    public int getInfoSize() { return infoSize; }

    public void setDescSize(int descSize) { this.descSize = descSize; }
    public int getDescSize() { return descSize; }


    @Override
    public String toString() {
        setDefaultSizing();

        StringBuilder sb = new StringBuilder();

        sb.append("{\n")
                .append("\tweek:").append(week).append("\n")
                .append("\tnum:").append(num).append("\n")
                .append("\ttitle:").append(title).append("\n")
                .append("\tsubtitle:").append(subtitle).append("\n")
                .append("\tinfo1:").append(info1).append("\n")
                .append("\tinfo2:").append(info2).append("\n")
                .append("\tinfo3:").append(info3).append("\n")
                .append("\tinfo4:").append(info4).append("\n")
                .append("\tdescription:").append(description).append("\n")
                .append("\tlink:").append(link).append("\n")
                .append("\ttitleSize:").append(titleSize).append("\n")
                .append("\tsubtitleSize:").append(subtitleSize).append("\n")
                .append("\tinfoSize:").append(infoSize).append("\n")
                .append("\tdescSize:").append(descSize).append("\n")
                .append("}\n");

        return sb.toString();
    }

    public void setDefaultSizing() {
        if (this.titleSize == 0) this.titleSize = 24;
        if (this.subtitleSize == 0) this.subtitleSize = 18;
        if (this.infoSize == 0) this.infoSize = 14;
        if (this.descSize == 0) this.descSize = 13;
    }
}
