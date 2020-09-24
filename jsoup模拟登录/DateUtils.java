package cn.com.ruijie.code.safety.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    /**
     * 将时间转换为时间戳
     */
    public static long dateToStamp(String s) throws Exception {
        Date date = formatTimeToDate(s);
        return date.getTime();
    }

    public static String timeStampToDateformat(long timestamp) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    public static String dateToDateformat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String getDateFormat(long timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(timestamp);
        return simpleDateFormat.format(date);
    }

    public static Date formatTimeToDate(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(s);
        return date;
    }

    public static Date reduceOneHour(String s) throws ParseException {
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = df.parse(s);
        Calendar cal=Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.HOUR, -1); //减
        return cal.getTime();
    }

    public static void main(String[] args) throws IOException {

//        login();
//        loginGerrit();
        parseGerritHtml();
    }

    public static String login() throws IOException {
        String url="https://sso.ruijie.net:8443/cas/login";

        Connection connection = Jsoup.connect(url);  // 获取connection
        connection.userAgent("Mozilla");   // 配置模拟浏览器
        Connection.Response response = connection.execute();                // 获取响应
        Document d1 = Jsoup.parse(response.body());       // 通过Jsoup将返回信息转换为Dom树
        List<Element> eleList = d1.select("form");

        // 获取cooking和表单属性
        // lets make data map containing all the parameters and its values found in the form
        Map<String, String> datas = new HashMap<>();
        for (Element e : eleList.get(0).getAllElements()) {
            // 设置用户名
            if (e.attr("name").equals("username")) {
                e.attr("value", "hguohua");
            }
            // 设置用户密码
            if (e.attr("name").equals("password")) {
                e.attr("value", "hgh123");
            }
            // 排除空值表单属性
            if (e.attr("name").length() > 0) {
                datas.put(e.attr("name"), e.attr("value"));
            }
        }

        /*
         * 第二次请求，以post方式提交表单数据以及cookie信息
         */
        Connection con2 = Jsoup.connect("https://sso.ruijie.net:8443/cas/login?service=http%3a%2f%2fgerrit.ruijie.work%2flogin%2fdashboard%2fself");
        con2.userAgent("Mozilla");   // 配置模拟浏览器
        // 设置cookie和post上面的map数据
        Connection.Response login = con2.ignoreContentType(true).followRedirects(true).method(Connection.Method.POST)
                .data(datas).cookies(response.cookies()).execute();
        // 打印，登陆成功后的信息
        System.out.println(login.body());
        System.out.println(eleList);

        /*
         * 第三次请求，以post方式提交表单数据以及cookie信息
         */
        Connection con3 = Jsoup.connect("http://gerrit.ruijie.work/login/dashboard/self");
        con2.userAgent("Mozilla");   // 配置模拟浏览器
        // 设置cookie和post上面的map数据
        Connection.Response gerritlogin = con3.ignoreContentType(true).followRedirects(true).method(Connection.Method.POST)
                .data(datas).cookies(login.cookies()).execute();
        // 打印，登陆成功后的信息
//        System.out.println(gerritlogin.body());
        String gerritAccount = gerritlogin.cookies().get("GerritAccount");
        return gerritAccount;
    }

    public static void loginGerrit() throws IOException {
        String url="https://sso.ruijie.net:8443/cas/login";
//        String url="https://sso.ruijie.net:8443/cas/login?service=http%3a%2f%2fgerrit.ruijie.work%2flogin%2fdashboard%2fself";
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", "hguohua");
        map.put("password", "hgh123");
        map.put("submit","");
        map.put("lt","e2s1");
        map.put("_eventId","submit");
        Connection.Response response = Jsoup.connect(url)
                .data(map)
                .method(Connection.Method.POST)
                .timeout(20000)
                .execute();

        String jsessionid = response.cookies().get("JSESSIONID");

        System.out.println(response);
    }

    public static String gerritAccount="";
    public static void parseGerritHtml() throws IOException {
        //        String url="https://news.163.com/";
        String url="http://gerrit.ruijie.work/plugins/gitiles/PD-Projects/+/aac32e407b452b6302d1002b317c265bbd36b4d6%5E%21/";
//        String headerCookie="GERRIT_UI=POLYGERRIT; GerritAccount=aSkkprsH1KVjzyOTfoVKQvlCWweaEjek1G;";
        gerritAccount=login();
        String headerCookie=String.format("GERRIT_UI=POLYGERRIT; GerritAccount=%s;",gerritAccount);

//        String url="http://192.168.160.128:8091/plugins/gitiles/notification/+/2a1b4063abbb8faea55c9dd07f278eed1b1c12b5%5E%21/";
//        String headerCookie="GERRIT_UI=POLYGERRIT; GerritAccount=aSgeprrxM-JiCpHQxWDgipM4CQxSeUMt-G;";
        //        Document document = Jsoup.connect(url).get();
//        Document document = Jsoup.connect(url).timeout(30000).userAgent("Mozilla").validateTLSCertificates(false).get();
        Connection connect = Jsoup.connect(url);
        connect.header("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        connect.header("Accept-Encoding","gzip, deflate");
        connect.header("Accept-Language","zh-CN,zh;q=0.9");
        connect.header("Cache-Control","max-age=0");
        connect.header("Connection","keep-alive");
        connect.header("Cookie",headerCookie);
        connect.header("Host","gerrit.ruijie.work");
        connect.header("Upgrade-Insecure-Requests","1");
//        connect.header("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        connect.userAgent("Mozilla");
        Document document = connect.get();

        //Cookie
        //	GerritAccount=aSkkprrrliOn4bFH31zsrjPBEcrCUn9H1G; XSRF_TOKEN=aSkkprqI35fYZIZy9nD1KuCRqXh4hZwgmW
        System.out.println(document);
    }

}
