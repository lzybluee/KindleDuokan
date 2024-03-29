import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rating {

	static class Book {
		String title;
		float size;
		float rating;
		int rates;

		public String toString() {
			return title + "\t" + rating + "\t" + rates + "\t" + size;
		}
	}

	public static Vector<Book> getList() {
		Vector<Book> list = new Vector<>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("kindle.txt"));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.contains(".mobi -> ") || line.contains(".epub -> ") || line.contains(".pdf -> ")) {
					Pattern p = Pattern.compile("^(.*\\\\)?(.*)\\.[^.]+ -> (.*)MB$");
					Matcher m = p.matcher(line);
					if (m.find()) {
						Book book = new Book();
						book.title = m.group(2);
						book.size = Float.parseFloat(m.group(3));
						list.add(book);
					}
				} else if (line.contains(".epub [")) {
					Pattern p = Pattern.compile("^.*\\.epub \\[(.*)\\] -> (.*)MB$");
					Matcher m = p.matcher(line);
					if (m.find()) {
						Book book = new Book();
						book.title = m.group(1);
						book.size = Float.parseFloat(m.group(2));
						list.add(book);
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	static String htmlEscape(String str) {
		str = str.replaceAll("&ldquo;", "“").replaceAll("&rdquo;", "”").replaceAll("&nbsp;", " ")
				.replaceAll("&amp;", "&").replaceAll("&#39;", "'").replaceAll("&rsquo;", "’").replaceAll("&mdash;", "—")
				.replaceAll("&ndash;", "–");
		return str;
	}

	static String readUrl(String http) throws Exception {
		String content = "";
		int retry = 0;
		while (retry < 3) {
			try {
				URL url = new URL(http);
				URLConnection URLconnection = url.openConnection();
				URLconnection.setRequestProperty("User-Agent",
						"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
				URLconnection.setConnectTimeout(60000);
				URLconnection.setReadTimeout(60000);
				HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
				int responseCode = httpConnection.getResponseCode();
				if (responseCode == HttpURLConnection.HTTP_OK) {
					InputStream in = httpConnection.getInputStream();
					InputStreamReader isr = new InputStreamReader(in);
					BufferedReader bufr = new BufferedReader(isr);
					String str;
					while ((str = bufr.readLine()) != null) {
						content += str + "\n";
					}
					bufr.close();
					break;
				}
			} catch (Exception e) {
			}
			retry++;
		}
		return content;

	}

	public static void main(String[] args) throws Exception {
		Vector<Book> list = getList();
		for (Book book : list) {
			String content = readUrl("https://www.douban.com/search?cat=1001&q="
					+ book.title.replaceAll("（.*）", "").replaceAll("^\\d+\\.", "").replace("多看文库·", "")
							.replaceAll(" ", "+").replace("霍金经典三部曲", "时间简史").replace("安·兰德作品集", "阿特拉斯耸耸肩")
							.replace("斯蒂芬金经典系列", "肖申克的救赎").replace("道金斯科学经典系列", "自私的基因").replace("世界文学名著", "安娜·卡列尼娜")
							.replace("何伟三部曲", "寻路中国").replace("最璀璨的银河", "流浪地球").replace("丹布朗作品集", "天使与魔鬼"));
			Pattern p = Pattern.compile("<span>\\[.*>(.*?) </a>");
			Matcher m = p.matcher(content);
			if (m.find()) {
				String title = htmlEscape(m.group(1));
				if (!title.equals(book.title)) {
					book.title += "=>" + title;
				}
			}
			p = Pattern.compile("<span class=\"rating_nums\">(.*?)</span>");
			m = p.matcher(content);
			if (m.find()) {
				book.rating = Float.parseFloat(m.group(1));
				p = Pattern.compile("<span>\\((\\d+)人评价\\)</span>");
				m = p.matcher(content);
				if (m.find()) {
					book.rates = Integer.parseInt(m.group(1));
				}
			}
			System.out.println(book);
			Thread.sleep(30000);
		}
		Collections.sort(list, new Comparator<Book>() {
			@Override
			public int compare(Book o1, Book o2) {
				if (o1.rating == o2.rating) {
					if (o1.rates == o2.rates) {
						return 0;
					}
					return o1.rates > o2.rates ? -1 : 1;
				}
				return o1.rating > o2.rating ? -1 : 1;
			}
		});
		System.out.println(
				"============================================================================================");
		for (Book book : list) {
			System.out.println(book);
		}
	}

}
