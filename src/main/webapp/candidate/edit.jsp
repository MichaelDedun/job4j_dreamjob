<%@ page import="ru.job4j.dream.model.Candidate" %>
<%@ page import="ru.job4j.dream.storage.PsqlStore" %>
<%@ page import="ru.job4j.dream.model.City" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c' %>
<!doctype html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css"
          integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.4.1.slim.min.js"
            integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n"
            crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js"
            integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js"
            integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6"
            crossorigin="anonymous"></script>

    <title>Работа мечты</title>
    <script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>

</head>
<body onload="getCities()">
<%
    String id = request.getParameter("id");
    Candidate candidate = new Candidate(0, "", 0L, 0);
    if (id != null) {
        candidate = PsqlStore.instOf().findCandidateById(Integer.parseInt(id));
    }
    String photoId = (String) request.getAttribute("photoId");
    if (photoId == null && candidate.getId() > 0) {
        photoId = String.valueOf(candidate.getPhotoId());
    }

%>
<script>
    function getCities() {
        $.ajax({
            type: "GET",
            url: "http://localhost:8080/dreamjob/city.do",
            dataType: 'json',
        })
            .done(function (data) {
                debugger;
                let cities = "";
                for (let i = 0; i < data.length; i++) {
                    cities += "<option value=" + data[i]['id'] + ">" + data[i]['name'] + "</option>";
                }
                $('#city').html(cities);
            })
            .fail(function (err) {
                alert("error" + err);
            })
    }
    function validate() {
        if ($('#name').val() === '' || $('#city').val() === '') {
            alert('Заполните форму');
            return false;
        }
        return true;
    }
</script>

<div class="container pt-3">
    <div class="row">
        <div class="card" style="width: 100%">
            <div class="card-header">
                <% if (id == null) {
                    if (photoId == null) { %>
                <a href='<c:redirect url="/upload"/>'></a>
                <% } else { %>
                Новый кандидат.
                <% }
                } else { %>
                Редактирование кандидата.
                <% } %>
            </div>
            <div class="card-body">
                <form action="<%=request.getContextPath()%>/candidates.do?id=<%=candidate.getId()%>" method="post">
                    <div class="form-group">
                        <label>Имя</label>
                        <input id="name" type="text" class="form-control" name="name" value="<%=candidate.getName()%>">
                        <input type="hidden" name="photoId" value="<%=photoId%>">
                    </div>
                    <div class="form-group">
                        <label for="city">Город:</label>
                        <select class="form-control" id="city" name="cityId">
                        </select>
                    </div>
                    <button type="submit" class="btn btn-primary" onclick="return validate()">Сохранить</button>
                </form>
            </div>
        </div>
    </div>
</div>

</body>
</html>