Queue Management Simulation
Această aplicație Java simulează gestionarea mai multor cozi cu clienți și servere folosind diferite strategii de alocare a sarcinilor.
Funcționalități
•	Simulare cu mai multe cozi și clienți.
•	Alocarea sarcinilor folosind strategii:
o	Shortest Queue – adaugă sarcina în coada cu cele mai puține elemente.
o	Shortest Time – adaugă sarcina în coada cu cel mai mic timp total de așteptare.
•	Interfață grafică pentru vizualizarea cozii și clienților.
•	Logarea evenimentelor și statistici finale (timp mediu de așteptare, timp mediu de servire, oră de vârf).
•	Teste predefinite și simulare personalizată.
Structură proiect
•	Model – clase pentru Task și Server.
•	BusinessLogic – clase pentru Scheduler, Strategy, SimulationManager.
•	GUI – interfața grafică (SimulationFrame, QueueAnimationPanel, Logger).
•	Main – punctul de intrare în aplicație.

