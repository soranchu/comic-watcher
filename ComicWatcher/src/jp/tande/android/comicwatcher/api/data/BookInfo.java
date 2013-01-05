package jp.tande.android.comicwatcher.api.data;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Bitmap;
import android.util.Log;

public class BookInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	private transient Bitmap thumb;
	private transient boolean thumbRequested = false;
	
	public String getBaseTitle() {
		return baseTitle;
	}


	public String getTitlePostFix() {
		return titlePostFix;
	}


	public int getVolume() {
		return volume;
	}

	private static final String TAG="BookInfo";
	public static class Item implements Serializable{
		private static final long serialVersionUID = 1L;
		@Override
		public String toString() {
			return "Item [title=" + title + ", titleKana=" + titleKana
					+ ", subTitle=" + subTitle + ", subTitleKana="
					+ subTitleKana + ", seriesName=" + seriesName
					+ ", seriesNameKana=" + seriesNameKana + ", contents="
					+ contents + ", author=" + author + ", authorKana="
					+ authorKana + ", publisherName=" + publisherName
					+ ", size=" + size + ", isbn=" + isbn + ", itemCaption="
					+ itemCaption + ", salesDate=" + salesDate + ", itemPrice="
					+ itemPrice + ", listPrice=" + listPrice
					+ ", discountRate=" + discountRate + ", discountPrice="
					+ discountPrice + ", itemUrl=" + itemUrl
					+ ", affiliateUrl=" + affiliateUrl + ", smallImageUrl="
					+ smallImageUrl + ", mediumImageUrl=" + mediumImageUrl
					+ ", largeImageUrl=" + largeImageUrl + ", chirayomiUrl="
					+ chirayomiUrl + ", availability=" + availability
					+ ", postageFlag=" + postageFlag + ", limitedFlag="
					+ limitedFlag + ", reviewCount=" + reviewCount
					+ ", reviewAverage=" + reviewAverage + ", booksGenreId="
					+ booksGenreId + "]";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((affiliateUrl == null) ? 0 : affiliateUrl.hashCode());
			result = prime * result
					+ ((author == null) ? 0 : author.hashCode());
			result = prime * result
					+ ((authorKana == null) ? 0 : authorKana.hashCode());
			result = prime * result
					+ ((availability == null) ? 0 : availability.hashCode());
			result = prime * result
					+ ((booksGenreId == null) ? 0 : booksGenreId.hashCode());
			result = prime * result
					+ ((chirayomiUrl == null) ? 0 : chirayomiUrl.hashCode());
			result = prime * result
					+ ((contents == null) ? 0 : contents.hashCode());
			result = prime * result + discountPrice;
			result = prime * result + discountRate;
			result = prime * result + ((isbn == null) ? 0 : isbn.hashCode());
			result = prime * result
					+ ((itemCaption == null) ? 0 : itemCaption.hashCode());
			result = prime * result + itemPrice;
			result = prime * result
					+ ((itemUrl == null) ? 0 : itemUrl.hashCode());
			result = prime * result
					+ ((largeImageUrl == null) ? 0 : largeImageUrl.hashCode());
			result = prime * result + limitedFlag;
			result = prime * result + listPrice;
			result = prime
					* result
					+ ((mediumImageUrl == null) ? 0 : mediumImageUrl.hashCode());
			result = prime * result + postageFlag;
			result = prime * result
					+ ((publisherName == null) ? 0 : publisherName.hashCode());
			result = prime * result
					+ ((reviewAverage == null) ? 0 : reviewAverage.hashCode());
			result = prime * result + reviewCount;
			result = prime * result
					+ ((salesDate == null) ? 0 : salesDate.hashCode());
			result = prime * result
					+ ((seriesName == null) ? 0 : seriesName.hashCode());
			result = prime
					* result
					+ ((seriesNameKana == null) ? 0 : seriesNameKana.hashCode());
			result = prime * result + ((size == null) ? 0 : size.hashCode());
			result = prime * result
					+ ((smallImageUrl == null) ? 0 : smallImageUrl.hashCode());
			result = prime * result
					+ ((subTitle == null) ? 0 : subTitle.hashCode());
			result = prime * result
					+ ((subTitleKana == null) ? 0 : subTitleKana.hashCode());
			result = prime * result + ((title == null) ? 0 : title.hashCode());
			result = prime * result
					+ ((titleKana == null) ? 0 : titleKana.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Item other = (Item) obj;
			if (affiliateUrl == null) {
				if (other.affiliateUrl != null)
					return false;
			} else if (!affiliateUrl.equals(other.affiliateUrl))
				return false;
			if (author == null) {
				if (other.author != null)
					return false;
			} else if (!author.equals(other.author))
				return false;
			if (authorKana == null) {
				if (other.authorKana != null)
					return false;
			} else if (!authorKana.equals(other.authorKana))
				return false;
			if (availability == null) {
				if (other.availability != null)
					return false;
			} else if (!availability.equals(other.availability))
				return false;
			if (booksGenreId == null) {
				if (other.booksGenreId != null)
					return false;
			} else if (!booksGenreId.equals(other.booksGenreId))
				return false;
			if (chirayomiUrl == null) {
				if (other.chirayomiUrl != null)
					return false;
			} else if (!chirayomiUrl.equals(other.chirayomiUrl))
				return false;
			if (contents == null) {
				if (other.contents != null)
					return false;
			} else if (!contents.equals(other.contents))
				return false;
			if (discountPrice != other.discountPrice)
				return false;
			if (discountRate != other.discountRate)
				return false;
			if (isbn == null) {
				if (other.isbn != null)
					return false;
			} else if (!isbn.equals(other.isbn))
				return false;
			if (itemCaption == null) {
				if (other.itemCaption != null)
					return false;
			} else if (!itemCaption.equals(other.itemCaption))
				return false;
			if (itemPrice != other.itemPrice)
				return false;
			if (itemUrl == null) {
				if (other.itemUrl != null)
					return false;
			} else if (!itemUrl.equals(other.itemUrl))
				return false;
			if (largeImageUrl == null) {
				if (other.largeImageUrl != null)
					return false;
			} else if (!largeImageUrl.equals(other.largeImageUrl))
				return false;
			if (limitedFlag != other.limitedFlag)
				return false;
			if (listPrice != other.listPrice)
				return false;
			if (mediumImageUrl == null) {
				if (other.mediumImageUrl != null)
					return false;
			} else if (!mediumImageUrl.equals(other.mediumImageUrl))
				return false;
			if (postageFlag != other.postageFlag)
				return false;
			if (publisherName == null) {
				if (other.publisherName != null)
					return false;
			} else if (!publisherName.equals(other.publisherName))
				return false;
			if (reviewAverage == null) {
				if (other.reviewAverage != null)
					return false;
			} else if (!reviewAverage.equals(other.reviewAverage))
				return false;
			if (reviewCount != other.reviewCount)
				return false;
			if (salesDate == null) {
				if (other.salesDate != null)
					return false;
			} else if (!salesDate.equals(other.salesDate))
				return false;
			if (seriesName == null) {
				if (other.seriesName != null)
					return false;
			} else if (!seriesName.equals(other.seriesName))
				return false;
			if (seriesNameKana == null) {
				if (other.seriesNameKana != null)
					return false;
			} else if (!seriesNameKana.equals(other.seriesNameKana))
				return false;
			if (size == null) {
				if (other.size != null)
					return false;
			} else if (!size.equals(other.size))
				return false;
			if (smallImageUrl == null) {
				if (other.smallImageUrl != null)
					return false;
			} else if (!smallImageUrl.equals(other.smallImageUrl))
				return false;
			if (subTitle == null) {
				if (other.subTitle != null)
					return false;
			} else if (!subTitle.equals(other.subTitle))
				return false;
			if (subTitleKana == null) {
				if (other.subTitleKana != null)
					return false;
			} else if (!subTitleKana.equals(other.subTitleKana))
				return false;
			if (title == null) {
				if (other.title != null)
					return false;
			} else if (!title.equals(other.title))
				return false;
			if (titleKana == null) {
				if (other.titleKana != null)
					return false;
			} else if (!titleKana.equals(other.titleKana))
				return false;
			return true;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getTitleKana() {
			return titleKana;
		}
		public void setTitleKana(String titleKana) {
			this.titleKana = titleKana;
		}
		public String getSubTitle() {
			return subTitle;
		}
		public void setSubTitle(String subTitle) {
			this.subTitle = subTitle;
		}
		public String getSubTitleKana() {
			return subTitleKana;
		}
		public void setSubTitleKana(String subTitleKana) {
			this.subTitleKana = subTitleKana;
		}
		public String getSeriesName() {
			return seriesName;
		}
		public void setSeriesName(String seriesName) {
			this.seriesName = seriesName;
		}
		public String getSeriesNameKana() {
			return seriesNameKana;
		}
		public void setSeriesNameKana(String seriesNameKana) {
			this.seriesNameKana = seriesNameKana;
		}
		public String getContents() {
			return contents;
		}
		public void setContents(String contents) {
			this.contents = contents;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getAuthorKana() {
			return authorKana;
		}
		public void setAuthorKana(String authorKana) {
			this.authorKana = authorKana;
		}
		public String getPublisherName() {
			return publisherName;
		}
		public void setPublisherName(String publisherName) {
			this.publisherName = publisherName;
		}
		public String getSize() {
			return size;
		}
		public void setSize(String size) {
			this.size = size;
		}
		public String getIsbn() {
			return isbn;
		}
		public void setIsbn(String isbn) {
			this.isbn = isbn;
		}
		public String getItemCaption() {
			return itemCaption;
		}
		public void setItemCaption(String itemCaption) {
			this.itemCaption = itemCaption;
		}
		public String getSalesDate() {
			return salesDate;
		}
		public void setSalesDate(String salesDate) {
			this.salesDate = salesDate;
		}
		public int getItemPrice() {
			return itemPrice;
		}
		public void setItemPrice(int itemPrice) {
			this.itemPrice = itemPrice;
		}
		public int getListPrice() {
			return listPrice;
		}
		public void setListPrice(int listPrice) {
			this.listPrice = listPrice;
		}
		public int getDiscountRate() {
			return discountRate;
		}
		public void setDiscountRate(int discountRate) {
			this.discountRate = discountRate;
		}
		public int getDiscountPrice() {
			return discountPrice;
		}
		public void setDiscountPrice(int discountPrice) {
			this.discountPrice = discountPrice;
		}
		public String getItemUrl() {
			return itemUrl;
		}
		public void setItemUrl(String itemUrl) {
			this.itemUrl = itemUrl;
		}
		public String getAffiliateUrl() {
			return affiliateUrl;
		}
		public void setAffiliateUrl(String affiliateUrl) {
			this.affiliateUrl = affiliateUrl;
		}
		public String getSmallImageUrl() {
			return smallImageUrl;
		}
		public void setSmallImageUrl(String smallImageUrl) {
			this.smallImageUrl = smallImageUrl;
		}
		public String getMediumImageUrl() {
			return mediumImageUrl;
		}
		public void setMediumImageUrl(String mediumImageUrl) {
			this.mediumImageUrl = mediumImageUrl;
		}
		public String getLargeImageUrl() {
			return largeImageUrl;
		}
		public void setLargeImageUrl(String largeImageUrl) {
			this.largeImageUrl = largeImageUrl;
		}
		public String getChirayomiUrl() {
			return chirayomiUrl;
		}
		public void setChirayomiUrl(String chirayomiUrl) {
			this.chirayomiUrl = chirayomiUrl;
		}
		public String getAvailability() {
			return availability;
		}
		public void setAvailability(String availability) {
			this.availability = availability;
		}
		public int getPostageFlag() {
			return postageFlag;
		}
		public void setPostageFlag(int postageFlag) {
			this.postageFlag = postageFlag;
		}
		public int getLimitedFlag() {
			return limitedFlag;
		}
		public void setLimitedFlag(int limitedFlag) {
			this.limitedFlag = limitedFlag;
		}
		public int getReviewCount() {
			return reviewCount;
		}
		public void setReviewCount(int reviewCount) {
			this.reviewCount = reviewCount;
		}
		public String getReviewAverage() {
			return reviewAverage;
		}
		public void setReviewAverage(String reviewAverage) {
			this.reviewAverage = reviewAverage;
		}
		public String getBooksGenreId() {
			return booksGenreId;
		}
		public void setBooksGenreId(String booksGenreId) {
			this.booksGenreId = booksGenreId;
		}
		private String title;
		private String titleKana;
		private String subTitle;
		private String subTitleKana;
		private String seriesName;
		private String seriesNameKana;
		private String contents;
		private String author;
		private String authorKana;
		private String publisherName;
		private String size;
		private String isbn;
		private String itemCaption;
		private String salesDate;
		private int itemPrice;
		private int listPrice;
		private int discountRate;
		private int discountPrice;
		private String itemUrl;
		private String affiliateUrl;
		private String smallImageUrl;
		private String mediumImageUrl;
		private String largeImageUrl;
		private String chirayomiUrl;
		private String availability;
		private int postageFlag;
		private int limitedFlag;
		private int reviewCount;
		private String reviewAverage;
		private String booksGenreId;
	}
	private Item item;
	private String baseTitle;
	private String titlePostFix;
	private int volume;
	private static Pattern pat = Pattern.compile(
			"(.*?)\\s*" +			//1 base title
			"(\\(|（)?\\s*" +		//2 brace
			"(第|巻|[vV]ol\\.?)?\\s*" +	//3
			"([0-9０-９]+)\\s*" + 	//4 number
			"巻?\\s*" + 				//
			"(\\)|）)?\\s*" +		//5 brace
			"([^0-9０-９]*)");		//6 postfix
//	private static Pattern pat = Pattern.compile("(.*)(\\（｜（)巻?([0-9]+|[０-９]+)巻?(\\)|）)");
	public void parseTitle(){
		String t = getTitle();
		Log.d(TAG,"parse:" + t);
		Matcher m = pat.matcher(t);
		if( m.matches() ){
			baseTitle = m.group(1);
			String num = m.group(4);
			titlePostFix = m.group(6);
			volume = Integer.parseInt(num);
			/*
			for (int i = 0; i<=m.groupCount() ; i++) {
				Log.d(TAG, "m:["+ m.group(i)+"]");
			}*/
			Log.d(TAG, "match title ["+ baseTitle +"] + vol:" + volume + " post:" + titlePostFix);
			
		}
		if(baseTitle == null ){
			baseTitle = t;
			volume = 1;
			titlePostFix = null;
		}
		baseTitle = baseTitle.trim().replace("　", " ");
		if( titlePostFix != null && titlePostFix.isEmpty() ){
			titlePostFix = null;
		}
	}
	
	
	public boolean equals(Object o) {
		return item.equals(o);
	}
	public String getTitle() {
		return item.getTitle();
	}
	public String getTitleKana() {
		return item.getTitleKana();
	}
	public String getSubTitle() {
		return item.getSubTitle();
	}
	public String getSubTitleKana() {
		return item.getSubTitleKana();
	}
	public String getSeriesName() {
		return item.getSeriesName();
	}
	public String getSeriesNameKana() {
		return item.getSeriesNameKana();
	}
	public String getContents() {
		return item.getContents();
	}
	public String getAuthor() {
		return item.getAuthor();
	}
	public String getAuthorKana() {
		return item.getAuthorKana();
	}
	public String getPublisherName() {
		return item.getPublisherName();
	}
	public String getSize() {
		return item.getSize();
	}
	public String getIsbn() {
		return item.getIsbn();
	}
	public String getItemCaption() {
		return item.getItemCaption();
	}
	public String getSalesDate() {
		return item.getSalesDate();
	}
	public int getItemPrice() {
		return item.getItemPrice();
	}
	public int getListPrice() {
		return item.getListPrice();
	}
	public int getDiscountRate() {
		return item.getDiscountRate();
	}
	public int getDiscountPrice() {
		return item.getDiscountPrice();
	}
	public String getItemUrl() {
		return item.getItemUrl();
	}
	public String getAffiliateUrl() {
		return item.getAffiliateUrl();
	}
	public String getSmallImageUrl() {
		return item.getSmallImageUrl();
	}
	public String getMediumImageUrl() {
		return item.getMediumImageUrl();
	}
	public String getLargeImageUrl() {
		return item.getLargeImageUrl();
	}
	public String getChirayomiUrl() {
		return item.getChirayomiUrl();
	}
	public String getAvailability() {
		return item.getAvailability();
	}
	public int getPostageFlag() {
		return item.getPostageFlag();
	}
	public int getLimitedFlag() {
		return item.getLimitedFlag();
	}
	public int getReviewCount() {
		return item.getReviewCount();
	}
	public String getReviewAverage() {
		return item.getReviewAverage();
	}
	public String getBooksGenreId() {
		return item.getBooksGenreId();
	}
	public int hashCode() {
		return item.hashCode();
	}
	public void setTitle(String title) {
		item.setTitle(title);
		parseTitle();
	}
	public void setTitleKana(String titleKana) {
		item.setTitleKana(titleKana);
	}
	public void setSubTitle(String subTitle) {
		item.setSubTitle(subTitle);
	}
	public void setSubTitleKana(String subTitleKana) {
		item.setSubTitleKana(subTitleKana);
	}
	public void setSeriesName(String seriesName) {
		item.setSeriesName(seriesName);
	}
	public void setSeriesNameKana(String seriesNameKana) {
		item.setSeriesNameKana(seriesNameKana);
	}
	public void setContents(String contents) {
		item.setContents(contents);
	}
	public void setAuthor(String author) {
		item.setAuthor(author);
	}
	public void setAuthorKana(String authorKana) {
		item.setAuthorKana(authorKana);
	}
	public void setPublisherName(String publisherName) {
		item.setPublisherName(publisherName);
	}
	public void setSize(String size) {
		item.setSize(size);
	}
	public void setIsbn(String isbn) {
		item.setIsbn(isbn);
	}
	public void setItemCaption(String itemCaption) {
		item.setItemCaption(itemCaption);
	}
	public void setSalesDate(String salesDate) {
		item.setSalesDate(salesDate);
	}
	public void setItemPrice(int itemPrice) {
		item.setItemPrice(itemPrice);
	}
	public void setListPrice(int listPrice) {
		item.setListPrice(listPrice);
	}
	public void setDiscountRate(int discountRate) {
		item.setDiscountRate(discountRate);
	}
	public void setDiscountPrice(int discountPrice) {
		item.setDiscountPrice(discountPrice);
	}
	public void setItemUrl(String itemUrl) {
		item.setItemUrl(itemUrl);
	}
	public void setAffiliateUrl(String affiliateUrl) {
		item.setAffiliateUrl(affiliateUrl);
	}
	public void setSmallImageUrl(String smallImageUrl) {
		item.setSmallImageUrl(smallImageUrl);
	}
	public void setMediumImageUrl(String mediumImageUrl) {
		item.setMediumImageUrl(mediumImageUrl);
	}
	public void setLargeImageUrl(String largeImageUrl) {
		item.setLargeImageUrl(largeImageUrl);
	}
	public void setChirayomiUrl(String chirayomiUrl) {
		item.setChirayomiUrl(chirayomiUrl);
	}
	public void setAvailability(String availability) {
		item.setAvailability(availability);
	}
	public void setPostageFlag(int postageFlag) {
		item.setPostageFlag(postageFlag);
	}
	public void setLimitedFlag(int limitedFlag) {
		item.setLimitedFlag(limitedFlag);
	}
	public void setReviewCount(int reviewCount) {
		item.setReviewCount(reviewCount);
	}
	public void setReviewAverage(String reviewAverage) {
		item.setReviewAverage(reviewAverage);
	}
	public void setBooksGenreId(String booksGenreId) {
		item.setBooksGenreId(booksGenreId);
	}
	
	public String[] getGenreIds(){
		if( getBooksGenreId() == null )return null;
		String g = getBooksGenreId();
		return g.split("/");
	}
	
	public String toString() {
		return item.toString();
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	
	public boolean isValidIsbn(){
		String isbn = getIsbn();
		//TODO: check check-digit
		return isbn != null && ( 
				isbn.length() == 13 && ( isbn.startsWith("9784") || isbn.startsWith("9794") ) 
				||isbn.length() == 10 && ( isbn.startsWith("4") || isbn.startsWith("4")));
	}
	
	public boolean genreContainsOnly(String genre){
		String[] gs = getGenreIds();
		if( gs == null || gs.length == 0)return false;
		
		for (String g : gs) {
			if( !g.startsWith(genre) ){
				return false;
			}
		}
		return true;
	}


	public Bitmap getThumb() {
		return thumb;
	}


	public void setThumb(Bitmap thumb) {
		this.thumb = thumb;
	}


	public boolean isThumbRequested() {
		return thumbRequested;
	}


	public void setThumbRequested(boolean thumbRequested) {
		this.thumbRequested = thumbRequested;
	}
}
