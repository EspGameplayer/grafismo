import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITeamStaffMember, TeamStaffMember } from '../team-staff-member.model';
import { TeamStaffMemberService } from '../service/team-staff-member.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { StaffMemberRole } from 'app/entities/enumerations/staff-member-role.model';

@Component({
  selector: 'jhi-team-staff-member-update',
  templateUrl: './team-staff-member-update.component.html',
})
export class TeamStaffMemberUpdateComponent implements OnInit {
  isSaving = false;
  staffMemberRoleValues = Object.keys(StaffMemberRole);

  peopleCollection: IPerson[] = [];
  teamsSharedCollection: ITeam[] = [];

  editForm = this.fb.group({
    id: [],
    role: [],
    person: [null, Validators.required],
    team: [],
  });

  constructor(
    protected teamStaffMemberService: TeamStaffMemberService,
    protected personService: PersonService,
    protected teamService: TeamService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ teamStaffMember }) => {
      this.updateForm(teamStaffMember);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const teamStaffMember = this.createFromForm();
    if (teamStaffMember.id !== undefined) {
      this.subscribeToSaveResponse(this.teamStaffMemberService.update(teamStaffMember));
    } else {
      this.subscribeToSaveResponse(this.teamStaffMemberService.create(teamStaffMember));
    }
  }

  trackPersonById(_index: number, item: IPerson): number {
    return item.id!;
  }

  trackTeamById(_index: number, item: ITeam): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITeamStaffMember>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(teamStaffMember: ITeamStaffMember): void {
    this.editForm.patchValue({
      id: teamStaffMember.id,
      role: teamStaffMember.role,
      person: teamStaffMember.person,
      team: teamStaffMember.team,
    });

    this.peopleCollection = this.personService.addPersonToCollectionIfMissing(this.peopleCollection, teamStaffMember.person);
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, teamStaffMember.team);
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query({ filter: 'teamstaffmember-is-null' })
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleCollection = people));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));
  }

  protected createFromForm(): ITeamStaffMember {
    return {
      ...new TeamStaffMember(),
      id: this.editForm.get(['id'])!.value,
      role: this.editForm.get(['role'])!.value,
      person: this.editForm.get(['person'])!.value,
      team: this.editForm.get(['team'])!.value,
    };
  }
}
