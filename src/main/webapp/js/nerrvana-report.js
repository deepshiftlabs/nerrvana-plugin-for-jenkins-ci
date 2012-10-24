function toggleMessages(id){
    var tr = document.getElementById(id);
    if(tr.v && tr.v == true){
        tr.v = false;
        tr.style.display = 'none';
    }
    else{
        tr.v = true;
        tr.style.display = 'table-row';
    }
}
