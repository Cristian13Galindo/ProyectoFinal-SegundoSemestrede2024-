(() => {
    const xhr = new XMLHttpRequest();

    xhr.open("GET", "ServletExample?option=1", true);

    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log(`La respuesta es ${xhr.responseText}`);
        }
    };
    xhr.send();
})();


document.querySelector("#btnSave").addEventListener('click', () => {
    const plate = document.querySelector("#plate").value;
    const brand = document.querySelector("#brand").value;
    const year = document.querySelector("#year").value;
    const color = document.querySelector("#color").value;
    console.log(`Datos del carro: plate=${plate}, brand=${brand}, year=${year}, color=${color}`);
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "ServletExample", true);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if(xhr.responseText !== "null"){
            const car = JSON.parse(xhr.responseText);
            console.log(`Placa=${car.plate}`);
            console.log(`Marca=${car.brand}`);
            console.log(`Modelo=${car.year}`);
            console.log(`Color=${car.color}`);
            }else{
            alert(alert("Carro no existe"));
            }
        }
    };
    xhr.setRequestHeader("Content-Type", "application/json");
    const data = JSON.stringify({ plate, brand, year, color });
    xhr.send(data);
});
document.querySelector("#btnPut").addEventListener('click', () => {

    const xhr = new XMLHttpRequest();
    xhr.open("PUT", "ServletExample", true);
    xhr.onreadystatechange = () => {
        if (xhr.readyState === 4 && xhr.status === 200) {
            if (xhr.responseText !== "No se encontró el carro") {
                const car = JSON.parse(xhr.responseText);
                console.log(`Carro actualizado: Placa=${car.plate}, Marca=${car.brand}, Modelo=${car.year}, Color=${car.color}`);
                alert("Carro actualizado exitosamente");
            } else {
                alert("Carro no encontrado");
            }
        }
    };
    xhr.setRequestHeader("Content-Type", "application/json");
    const data = JSON.stringify({ plate, brand, year, color });
    xhr.send(data);
});