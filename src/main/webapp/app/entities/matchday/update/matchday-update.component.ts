import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMatchday, Matchday } from '../matchday.model';
import { MatchdayService } from '../service/matchday.service';
import { ICompetition } from 'app/entities/competition/competition.model';
import { CompetitionService } from 'app/entities/competition/service/competition.service';

@Component({
  selector: 'jhi-matchday-update',
  templateUrl: './matchday-update.component.html',
})
export class MatchdayUpdateComponent implements OnInit {
  isSaving = false;

  competitionsSharedCollection: ICompetition[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    graphicsName: [],
    number: [null, [Validators.min(0)]],
    competition: [],
  });

  constructor(
    protected matchdayService: MatchdayService,
    protected competitionService: CompetitionService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matchday }) => {
      this.updateForm(matchday);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const matchday = this.createFromForm();
    if (matchday.id !== undefined) {
      this.subscribeToSaveResponse(this.matchdayService.update(matchday));
    } else {
      this.subscribeToSaveResponse(this.matchdayService.create(matchday));
    }
  }

  trackCompetitionById(_index: number, item: ICompetition): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMatchday>>): void {
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

  protected updateForm(matchday: IMatchday): void {
    this.editForm.patchValue({
      id: matchday.id,
      name: matchday.name,
      graphicsName: matchday.graphicsName,
      number: matchday.number,
      competition: matchday.competition,
    });

    this.competitionsSharedCollection = this.competitionService.addCompetitionToCollectionIfMissing(
      this.competitionsSharedCollection,
      matchday.competition
    );
  }

  protected loadRelationshipsOptions(): void {
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

  protected createFromForm(): IMatchday {
    return {
      ...new Matchday(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      graphicsName: this.editForm.get(['graphicsName'])!.value,
      number: this.editForm.get(['number'])!.value,
      competition: this.editForm.get(['competition'])!.value,
    };
  }
}
