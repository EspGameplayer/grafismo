import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDeduction, Deduction } from '../deduction.model';
import { DeductionService } from '../service/deduction.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

@Component({
  selector: 'jhi-deduction-update',
  templateUrl: './deduction-update.component.html',
})
export class DeductionUpdateComponent implements OnInit {
  isSaving = false;

  teamsSharedCollection: ITeam[] = [];
  competitionsSharedCollection: ICompetition[] = [];

  editForm = this.fb.group({
    id: [],
    points: [null, [Validators.min(1)]],
    moment: [],
    reason: [],
    team: [null, Validators.required],
    competition: [null, Validators.required],
  });

  constructor(
    protected deductionService: DeductionService,
    protected teamService: TeamService,
    protected competitionService: CompetitionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deduction }) => {
      if (deduction.id === undefined) {
        const today = dayjs().startOf('day');
        deduction.moment = today;
      }

      this.updateForm(deduction);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const deduction = this.createFromForm();
    if (deduction.id !== undefined) {
      this.subscribeToSaveResponse(this.deductionService.update(deduction));
    } else {
      this.subscribeToSaveResponse(this.deductionService.create(deduction));
    }
  }

  trackTeamById(_index: number, item: ITeam): number {
    return item.id!;
  }

  trackCompetitionById(_index: number, item: ICompetition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDeduction>>): void {
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

  protected updateForm(deduction: IDeduction): void {
    this.editForm.patchValue({
      id: deduction.id,
      points: deduction.points,
      moment: deduction.moment ? deduction.moment.format(DATE_TIME_FORMAT) : null,
      reason: deduction.reason,
      team: deduction.team,
      competition: deduction.competition,
    });

    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, deduction.team);
    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing(
      this.competitionsSharedCollection,
      deduction.competition
    );
  }

  protected loadRelationshipsOptions(): void {
    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.competitionService
      .query()
      .pipe(map((res: HttpResponse<ICompetition[]>) => res.body ?? []))
      .pipe(
        map((competitions: ICompetition[]) =>
          this.competitionService.addCompetitionToCollectionIfMissing(competitions, this.editForm.get('competition')!.value)
        )
      )
      .subscribe((competitions: ICompetition[]) => (this.competitionsSharedCollection = competitions));
  }

  protected createFromForm(): IDeduction {
    return {
      ...new Deduction(),
      id: this.editForm.get(['id'])!.value,
      points: this.editForm.get(['points'])!.value,
      moment: this.editForm.get(['moment'])!.value ? dayjs(this.editForm.get(['moment'])!.value, DATE_TIME_FORMAT) : undefined,
      reason: this.editForm.get(['reason'])!.value,
      team: this.editForm.get(['team'])!.value,
      competition: this.editForm.get(['competition'])!.value,
    };
  }
}
