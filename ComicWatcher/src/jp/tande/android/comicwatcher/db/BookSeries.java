package jp.tande.android.comicwatcher.db;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.tande.android.comicwatcher.db.DatabaseManager.Contract;
import android.content.ContentValues;
import android.database.Cursor;

public class BookSeries implements Serializable{
	private static final long serialVersionUID = 7893319474968721635L;

	private boolean poplulated;
	private long seriesId;
	
	private List<BookInfo> books = new ArrayList<BookInfo>();
	private List<String> ignoredBookIsbns = new ArrayList<String>();
	private int ownedCount = 0;
	private int totalNotIgnored = 0;

	@Deprecated BookInfo latest;
	private int latestVolume =0;
	private int latestAvailability =0;
	
	private String author;
	private String title;
	private String publisher;
	private String booksGenreId;
	private String imageUrl;
	private String latestSalesDate;

	public BookSeries(BookInfo bi) {
		title = bi.getBaseTitle();
		publisher = bi.getPublisherName();
		author = bi.getAuthor();
		booksGenreId = bi.getBooksGenreId();
		addBook(bi);
		poplulated = true;
	}
	
	@Deprecated
	/*package*/ BookSeries(long seriesId, String title, String publisher, String author, int ownedCount, int totalNotIgnored){
		this.seriesId = seriesId;
		this.title = title;
		this.publisher = publisher;
		this.author = author;
		this.ownedCount = ownedCount;
		this.totalNotIgnored = totalNotIgnored;
		poplulated = false;
	}
	
	private BookSeries(){
		poplulated = false;
	}
	
	public static BookSeries fromCursor(Cursor c){
		BookSeries bs = new BookSeries();
		bs.seriesId = c.getLong(c.getColumnIndex(Contract.Follows.Columns._ID));
		bs.title = c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_TITLE));
		bs.publisher = c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_PUBLISHER));
		bs.author = c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_AUTHOR));
		bs.ownedCount = c.getInt(c.getColumnIndex(Contract.Follows.Columns.COL_OWNED_COUNT));
		bs.totalNotIgnored = c.getInt(c.getColumnIndex(Contract.Follows.Columns.COL_TOTAL_NOT_IGNORED_COUNT));

		//latest book
		bs.imageUrl = c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_IMAGE_URL));
		bs.latestSalesDate = c.getString(c.getColumnIndex(Contract.Follows.Columns.COL_LATEST_SALES_DATE));
		bs.latestAvailability = c.getInt(c.getColumnIndex(Contract.Follows.Columns.COL_AVAILABILITY));
		bs.latestVolume = c.getInt(c.getColumnIndex(Contract.Follows.Columns.COL_VOLUMES));
		return bs;

	}
	
	public List<BookInfo> getBooks() {
		return books;
	}
	
	public void addBook(BookInfo bi){
		books.add(bi);
		if( bi.getVolume() >= latestVolume && bi.getTitlePostFix() == null ){
			latest = bi;
			latestVolume = bi.getVolume();
			latestAvailability = Integer.parseInt( bi.getAvailability() );
			latestSalesDate = bi.getSalesDate();
			imageUrl = bi.getMediumImageUrl();
		}
		if( ! isIgnored(bi) ){
			totalNotIgnored++;
			if( bi.isOwned() ){
				ownedCount ++;
			}
		}
	}
	
	public void addIgnored(String isbn){
		ignoredBookIsbns.add(isbn);
		for (BookInfo bi : books) {
			if( isbn.equalsIgnoreCase(bi.getIsbn()) ){
				totalNotIgnored--;
				if( bi.isOwned() ){
					ownedCount--;
				}
			}
		}
	}
	
	private boolean isIgnored(BookInfo bi){
		for (String isbn : ignoredBookIsbns) {
			if( isbn.equalsIgnoreCase(bi.getIsbn())){
				return true;
			}
		}
		return false;
	}
	
	public int getLatestVolume() {
		return latestVolume;
	}
	public void setLatestVolume(int latestVolume) {
		this.latestVolume = latestVolume;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPublisher() {
		return publisher;
	}
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	public String getBookGenreId() {
		return booksGenreId;
	}
	
	public boolean match(BookInfo other){
		if( other == null )return false;

		if (author == null) {
			if (other.getAuthor() != null)
				return false;
		} else if (!author.equals(other.getAuthor()))
			return false;
		/*if (booksGenreId == null) {
			if (other.getBooksGenreId() != null)
				return false;
		} else if (!booksGenreId.equals(other.getBooksGenreId()))
			return false;*/
		if (publisher == null) {
			if (other.getPublisherName() != null)
				return false;
		} else if (!publisher.equals(other.getPublisherName()))
			return false;
		/*if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;*/
		if (title == null) {
			if (other.getBaseTitle() != null)
				return false;
		} else if (!title.equals(other.getBaseTitle()))
			return false;
		return true;

	}
	
	@Deprecated
	public BookInfo getLatest() {
		return latest;
	}

	public String getLatestSalesDate(){
		return latestSalesDate;
	}
	
	public void setLatestSalesDate(String date){
		this.latestSalesDate = date;
	}
	
	public String getBooksGenreId() {
		return booksGenreId;
	}

	public boolean isPoplulated() {
		return poplulated;
	}

	public long getSeriesId() {
		return seriesId;
	}

	void setSeriesId(long seriesId) {
		this.seriesId = seriesId;
	}

	void setPoplulated(boolean poplulated) {
		this.poplulated = poplulated;
	}

	public int getOwnedCount() {
		return ownedCount;
	}
	
	public int getTotalCount(){
		return totalNotIgnored;
	}

	public void setOwnedCount(int owned) {
		this.ownedCount = owned;
	}

	public List<String> getIgnoredBookIsbns() {
		return ignoredBookIsbns;
	}
	
	public String getImageUrl(){
		return imageUrl;
	}
	public void setImageUrl(String url){
		this.imageUrl = url;
	}
	
	public boolean isLatestOnSale(){
		return 5 != latestAvailability;
	}

	public ContentValues toContentValues() {
		ContentValues v = new ContentValues();
		if( getSeriesId() > 0 ){
			v.put(Contract.Follows.Columns._ID, getSeriesId());
		}
		v.put(Contract.Follows.Columns.COL_TITLE, getTitle());
		v.put(Contract.Follows.Columns.COL_AUTHOR, getAuthor());
		v.put(Contract.Follows.Columns.COL_PUBLISHER, getPublisher());
		v.put(Contract.Follows.Columns.COL_LAST_UPDATE, new Date().getTime());
		v.put(Contract.Follows.Columns.COL_VOLUMES, latestVolume);
		v.put(Contract.Follows.Columns.COL_OWNED_COUNT, getOwnedCount());
		v.put(Contract.Follows.Columns.COL_IMAGE_URL, getImageUrl());
		v.put(Contract.Follows.Columns.COL_LATEST_SALES_DATE, getLatestSalesDate());
		v.put(Contract.Follows.Columns.COL_TOTAL_NOT_IGNORED_COUNT, getTotalCount());
		v.put(Contract.Follows.Columns.COL_AVAILABILITY, latestAvailability);
		return v;
	}

	public ContentValues[] booksToContentValues() {
		if( books == null || books.isEmpty() ){
			return null;
		}
		
		ArrayList<ContentValues> values = new ArrayList<ContentValues>();

		for (BookInfo bi : books) {
			values.add(bi.toContentValues());
		}
		return values.toArray(new ContentValues[]{});
	}

	@Override
	public String toString() {
		return "BookSeries [poplulated=" + poplulated + ", seriesId="
				+ seriesId + ", books=" + books + ", ignoredBookIsbns="
				+ ignoredBookIsbns + ", ownedCount=" + ownedCount
				+ ", totalNotIgnored=" + totalNotIgnored + ", latest=" + latest
				+ ", latestVolume=" + latestVolume + ", author=" + author
				+ ", title=" + title + ", publisher=" + publisher
				+ ", booksGenreId=" + booksGenreId + ", imageUrl=" + imageUrl
				+ ", latestSalesDate=" + latestSalesDate + "]";
	}
	
}
