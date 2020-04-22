package com.danqing.pojo;

/**
 * Description: shiro_ssm
 * Created by danqing on 2020/4/22 16:48
 */
public class searchBean {
    private String searchItem;
    private String searchContent;
    private int page;
    private int nums;

    @Override
    public String toString() {
        return "searchBean{" +
                "searchItem='" + searchItem + '\'' +
                ", searchContent='" + searchContent + '\'' +
                ", page=" + page +
                ", nums=" + nums +
                '}';
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public String getSearchItem() {
        return searchItem;
    }

    public void setSearchItem(String searchItem) {
        this.searchItem = searchItem;
    }

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }
}
