package mx.com.ovaldez.lambda.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private String userId;
    private String nombre;
    private String apellido;
    private String condominio;
    private int antiguedad;
}
