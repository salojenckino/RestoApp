
package com.RestoApp2.web.Repositorios;

import com.RestoApp2.web.Entidades.Resto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *--extends-- la palabra clave se utiliza para implementar el concepto de herencia en el lenguaje de programación Java. 
 * La herencia esencialmente proporciona la reutilización del código al permitir extender las propiedades y el comportamiento de una clase existente por una clase recién definida. 
 * Cuando una nueva subclase (o clase derivada) extiende una superclase (o clase primaria), esa subclase heredará todos los atributos y métodos de la superclase. 
 * La subclase puede anular opcionalmente el comportamiento (proporcionar funcionalidad nueva o extendida a los métodos) heredada de la clase principal.
 * Una subclase no puede extender varias súper clases en Java. Por lo tanto, no puede usar extensiones para herencia múltiple. 
 * 
 * 
 * La palabra clave Implements se usa para que una clase implemente una interfaz determinada, 
 * mientras que la palabra clave Extends se usa para que una subclase se extienda desde una súper clase.
 * Cuando una clase implementa una interfaz(implements), esa clase necesita implementar todos los métodos definidos en 
 * la interfaz, pero cuando una subclase extiende una súper clase, puede o no reemplazar los métodos incluidos
 * en la clase primaria.
 * 
 *  Finalmente, otra diferencia clave entre Implements y Extends es que una clase puede implementar múltiples interfaces, pero solo puede extenderse desde una súper clase en Java. 
 * En general, el uso de Implementos (interfaces) se considera más favorable en comparación con el uso de Extensiones (herencia), por varias razones, como una mayor flexibilidad y
 * la capacidad de minimizar el acoplamiento. Por lo tanto, en la práctica, la programación a una interfaz es preferible a la extensión desde las clases base.
 * @author Federico
 */
@Repository
public interface RestoRepositorio extends JpaRepository<Resto,String> {
    
}
