import React from 'react';

export default function RulesText() {
  return (
    <div className="text-center" style={{marginTop: "70px"}}>
      
      <h1>Rules</h1>
      <p style={{ fontSize: '16px' }}>
        Aquí puedes encontrar las reglas de la aplicación. <strong>Resumen del juego Dwarf:</strong>
        <br />
        <br />
        <div style={{ textAlign: 'text-center' }}>
          1. <strong>Objetivo del juego:</strong>
        </div>
        <div style={{ textAlign: 'left' }}>
          <ul>
            <li>Juego multijugador para 2 o 3 personas con temática fantástica.</li>
            <li>El objetivo es convertirse en el "Héroe bajo la montaña."</li>
          </ul>
        </div>
        <br />
        <div style={{ textAlign: 'text-center' }}>
          2. <strong>Configuración del juego:</strong>
        </div>
        <div style={{ textAlign: 'left' }}>
          <ul>
            <li>Escenario con nueve cartas dispuestas en una matriz 3x3.</li>
            <li>Se juegan seis rondas, cada una con tres fases: Extracción de Minerales, Selección de Acciones y Resolución de Acciones.</li>
          </ul>
        </div>
        <br />
        <div style={{ textAlign: 'text-center' }}>
          3. <strong>Desarrollo del juego:</strong>
        </div>
        <div style={{ textAlign: 'left' }}>
          <ul>
            <li>Acumular materiales (hierro, oro, acero), medallas y objetos a lo largo de las fases.</li>
            <li>La partida termina cuando se han jugado todas las cartas o un jugador obtiene cuatro objetos.</li>
            <li>El ganador se determina por la mayoría de acero, oro y objetos. En caso de empate, se consideran medallas, hierro y objetos en ese orden.</li>
          </ul>
        </div>
        <br />
        <div style={{ textAlign: 'text-center' }}>
          4. <strong>Elementos del juego:</strong>
        </div>
        <div style={{ textAlign: 'left' }}>
          <ul>
            <li>Ficha de jugador inicial: indica el jugador que inicia la partida y cada ronda.</li>
            <li>Tres grupos de 4 enanos de colores diferentes para cada jugador.</li>
            <li>Mazo de cartas con acciones para la Fase de Extracción de Minerales.</li>
            <li>Materiales ilimitados: hierro, oro y acero.</li>
            <li>Medallas obtenidas al enfrentarse a enemigos durante la Fase de Resolución de Acciones.</li>
            <li>Objetos que se crean o compran con materiales; la partida termina cuando un jugador tiene cuatro objetos.</li>
          </ul>
        </div>
        <br />
        <div style={{ textAlign: 'text-center' }}>
          5. <strong>Fases del juego:</strong>
        </div>
        <div style={{ textAlign: 'left' }}>
          <ul>
            <li><strong>Fase de Extracción de Minerales:</strong>
              <ul>
                <li>Se toman dos cartas del mazo y se colocan en el escenario.</li>
                <li>Si la segunda carta coincide con la posición de la primera, se descarta y se elige otra.</li>
              </ul>
            </li>
            <li><strong>Fase de Selección de Acciones:</strong>
              <ul>
                <li>Los jugadores eligen entre colocar dos enanos en cartas no ocupadas o usar una carta de ayuda (rechazando el derecho de colocar enanos o gastando cuatro medallas).</li>
              </ul>
            </li>
            <li><strong>Fase de Resolución de Acciones:</strong>
              <ul>
                <li>Se resuelven las cartas de ayuda, si se usaron.</li>
                <li>Se resuelven las cartas ocupadas por los enanos de arriba abajo y de izquierda a derecha.</li>
              </ul>
            </li>
          </ul>
        </div>
      </p>
    </div>
  );
}
