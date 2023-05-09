package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/posts")
public class PostServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    out.println("<html>");
    out.println("<head>");
    out.println("<title>掲示板</title>");
    out.println("</head>");
    out.println("<body>");
    out.println("<h1>掲示板</h1>");
    out.println("<form method='POST' action='posts'>");
    out.println("<input type='text' name='title' placeholder='タイトル'><br>");
    out.println("<textarea name='content' placeholder='内容'></textarea><br>");
    out.println("<input type='submit' value='投稿する'>");
    out.println("</form>");
    out.println("<hr>");

    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample_board", "root", "");
      Statement stmt = conn.createStatement();
      ResultSet rs = stmt.executeQuery("SELECT * FROM posts ORDER BY created_at DESC");
      while (rs.next()) {
        out.println("<h2>" + rs.getString("title") + "</h2>");
        out.println("<p>" + rs.getString("content") + "</p>");
        out.println("<hr>");
      }
      rs.close();
      stmt.close();
      conn.close();
    } catch (Exception e) {
      out.println("エラーが発生しました。");
      e.printStackTrace(out);
    }

    out.println("</body>");
    out.println("</html>");
  }

  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    String title = request.getParameter("title");
    String content = request.getParameter("content");
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sample_board", "root", "");
      PreparedStatement stmt = conn.prepareStatement("INSERT INTO posts (title, content) VALUES (?, ?)");
      stmt.setString(1, title);
      stmt.setString(2, content);
      stmt.executeUpdate();
      stmt.close();
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    response.sendRedirect(request.getContextPath() + "/posts");
  } 
}