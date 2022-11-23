import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ISuspension, Suspension } from '../suspension.model';
import { SuspensionService } from '../service/suspension.service';
import { IPerson } from 'app/entities/person/person.model';
import { PersonService } from 'app/entities/person/service/person.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

@Component({
  selector: 'jhi-suspension-update',
  templateUrl: './suspension-update.component.html',
})
export class SuspensionUpdateComponent implements OnInit {
  isSaving = false;

  peopleSharedCollection: IPerson[] = [];
  competitionsSharedCollection: ICompetition[] = [];

  editForm = this.fb.group({
    id: [],
    matches: [null, [Validators.min(1)]],
    moment: [],
    reason: [],
    person: [null, Validators.required],
    competition: [null, Validators.required],
  });

  constructor(
    protected suspensionService: SuspensionService,
    protected personService: PersonService,
    protected competitionService: CompetitionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ suspension }) => {
      if (suspension.id === undefined) {
        const today = dayjs().startOf('day');
        suspension.moment = today;
      }

      this.updateForm(suspension);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const suspension = this.createFromForm();
    if (suspension.id !== undefined) {
      this.subscribeToSaveResponse(this.suspensionService.update(suspension));
    } else {
      this.subscribeToSaveResponse(this.suspensionService.create(suspension));
    }
  }

  trackPersonById(_index: number, item: IPerson): number {
    return item.id!;
  }

  trackCompetitionById(_index: number, item: ICompetition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISuspension>>): void {
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

  protected updateForm(suspension: ISuspension): void {
    this.editForm.patchValue({
      id: suspension.id,
      matches: suspension.matches,
      moment: suspension.moment ? suspension.moment.format(DATE_TIME_FORMAT) : null,
      reason: suspension.reason,
      person: suspension.person,
      competition: suspension.competition,
    });

    this.peopleSharedCollection = this.personService.addPersonToCollectionIfMissing(this.peopleSharedCollection, suspension.person);
    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing(
      this.competitionsSharedCollection,
      suspension.competition
    );
  }

  protected loadRelationshipsOptions(): void {
    this.personService
      .query()
      .pipe(map((res: HttpResponse<IPerson[]>) => res.body ?? []))
      .pipe(map((people: IPerson[]) => this.personService.addPersonToCollectionIfMissing(people, this.editForm.get('person')!.value)))
      .subscribe((people: IPerson[]) => (this.peopleSharedCollection = people));

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

  protected createFromForm(): ISuspension {
    return {
      ...new Suspension(),
      id: this.editForm.get(['id'])!.value,
      matches: this.editForm.get(['matches'])!.value,
      moment: this.editForm.get(['moment'])!.value ? dayjs(this.editForm.get(['moment'])!.value, DATE_TIME_FORMAT) : undefined,
      reason: this.editForm.get(['reason'])!.value,
      person: this.editForm.get(['person'])!.value,
      competition: this.editForm.get(['competition'])!.value,
    };
  }
}
