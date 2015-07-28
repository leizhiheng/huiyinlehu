package com.huiyin.bean;

import java.util.ArrayList;
import java.util.List;

public class HomeBean {

	/**广告轮换图**/
	private List<GalleryAd> listGallerys;

	/**快捷服务导航(今日头条，乐虎彩票，水电煤..等等)**/
	private List<ChannelItem> listChannelItems;
	
	/**快捷服务导航的图标**/
	private String fastImg;

	/**排行榜**/
	private List<TopList> listTopLists;

	/**分类聚合**/
	private List<HomePoly> listPolies;

	/**今日秒杀**/
	private HomeSeckillBean seckillList;

	/**促销**/
	private List<SalesPromotion> listSalesPromotions;

	/**闪购**/
	private HomeFlashBean mHomeFlashBean;

	/**新品预约**/
	private ProductBespeakBean productBespeakBean;

	public List<GalleryAd> getListGallerys() {
		if (listGallerys == null) {
			listGallerys = new ArrayList<GalleryAd>();
		}
		return listGallerys;
	}

	public void setListGallerys(List<GalleryAd> listGallerys) {
		this.listGallerys = listGallerys;
	}

	public List<ChannelItem> getListChannelItems() {
		return listChannelItems;
	}

	public void setListChannelItems(List<ChannelItem> listChannelItems) {
		this.listChannelItems = listChannelItems;
	}

	public List<TopList> getListTopLists() {
		return listTopLists;
	}

	public void setListTopLists(List<TopList> listTopLists) {
		this.listTopLists = listTopLists;
	}

	public List<SalesPromotion> getListSalesPromotions() {
		return listSalesPromotions;
	}

	public void setListSalesPromotions(List<SalesPromotion> listSalesPromotions) {
		this.listSalesPromotions = listSalesPromotions;
	}

	public List<HomePoly> getListPolies() {
		return listPolies;
	}

	public void setListPolies(List<HomePoly> listPolies) {
		this.listPolies = listPolies;
	}

	public HomeSeckillBean getSeckillList() {
		return seckillList;
	}

	public void setSeckillList(HomeSeckillBean seckillList) {
		this.seckillList = seckillList;
	}

	public HomeFlashBean getmHomeFlashBean() {
		return mHomeFlashBean;
	}

	public void setmHomeFlashBean(HomeFlashBean mHomeFlashBean) {
		this.mHomeFlashBean = mHomeFlashBean;
	}

	public ProductBespeakBean getProductBespeakBean() {
		return productBespeakBean;
	}

	public void setProductBespeakBean(ProductBespeakBean productBespeakBean) {
		this.productBespeakBean = productBespeakBean;
	}
	
	public String getFastImg() {
		return fastImg;
	}

	public void setFastImg(String fastImg) {
		this.fastImg = fastImg;
	}

	@Override
	public String toString() {
		return "HomeBean [listGallerys=" + listGallerys + ", listChannelItems="
				+ listChannelItems + ", listTopLists=" + listTopLists
				+ ", listPolies=" + listPolies + ", listSalesPromotions="
				+ listSalesPromotions + "]";
	}

}
