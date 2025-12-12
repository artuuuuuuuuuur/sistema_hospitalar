package classe;

import java.time.LocalDate;

public abstract class Agendamento {
    protected int id;
    protected LocalDate data;
    protected String status;
    protected Paciente paciente;
    protected Medico medico;

   
    public Agendamento(int id, LocalDate data, Paciente paciente, Medico medico) {
        this.id = id;
        this.data = data;
        this.paciente = paciente;
        this.medico = medico;
        this.status = "Pendente"; 
    }


    public int getId() {
        return id;
    }

    public LocalDate getData() {
        return data;
    }

    public String getStatus() {
        return status;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Medico getMedico() {
        return medico;
    }

   
    public abstract void reservar();

    public abstract void cancelar();
}


class AgendamentoConsulta extends Agendamento {

    public AgendamentoConsulta(int id, LocalDate data, Paciente paciente, Medico medico) {
        super(id, data, paciente, medico);
    }

    @Override
    public void reservar() {
        this.status = "Reservado";
        System.out.println("Consulta reservada para o paciente: " + paciente.getNome());
    }

    @Override
    public void cancelar() {
        this.status = "Cancelado";
        System.out.println("Consulta cancelada para o paciente: " + paciente.getNome());
    }
}


class AgendamentoExame extends Agendamento {
    private String tipoExame;

    public AgendamentoExame(int id, LocalDate data, Paciente paciente, Medico medico, String tipoExame) {
        super(id, data, paciente, medico);
        this.tipoExame = tipoExame;
    }

    // Getter e Setter para o tipo de exame
    public String getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(String tipoExame) {
        this.tipoExame = tipoExame;
    }

    @Override
    public void reservar() {
        this.status = "Reservado";
        System.out.println("Exame de " + tipoExame + " reservado para o paciente: " + paciente.getNome());
    }

    @Override
    public void cancelar() {
        this.status = "Cancelado";
        System.out.println("Exame de " + tipoExame + " cancelado para o paciente: " + paciente.getNome());
    }
}
